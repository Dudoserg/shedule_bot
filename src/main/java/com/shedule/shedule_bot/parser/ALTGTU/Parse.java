package com.shedule.shedule_bot.parser.ALTGTU;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shedule.shedule_bot.entity.Db.*;
import com.shedule.shedule_bot.parser.GroupInfo;
import com.shedule.shedule_bot.parser.Shedule_parser;
import com.shedule.shedule_bot.parser.WorkQueue;
import com.shedule.shedule_bot.repo.*;
import com.shedule.shedule_bot.service.RepoService.*;
import javafx.util.Pair;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class Parse {

    final
    SheduleRepo sheduleRepo;

    final
    FacultyRepo facultyRepo;

    final
    TimeSubjectService timeSubjectService;

    final
    TeacherRangService teacherRangService;

    final
    SubjectService subjectService;

    final
    TeacherService teacherService;

    final
    DayService dayService;

    final
    CabinetService cabinetService;

    final
    SubjectTypeService subjectTypeService;

    final
    GroupRepo groupRepo;

    List<String> daysList = new ArrayList<>(Arrays.asList(
            "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"
    ));

    public Parse(SheduleRepo sheduleRepo, FacultyRepo facultyRepo, TimeSubjectService timeSubjectService, TeacherRangService teacherRangService, SubjectService subjectService, TeacherService teacherService, DayService dayService, CabinetService cabinetService, SubjectTypeService subjectTypeService, GroupRepo groupRepo) throws JsonProcessingException, InterruptedException {
        this.sheduleRepo = sheduleRepo;
        this.facultyRepo = facultyRepo;
        this.timeSubjectService = timeSubjectService;
        this.teacherRangService = teacherRangService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.dayService = dayService;
        this.cabinetService = cabinetService;
        this.subjectTypeService = subjectTypeService;
        this.groupRepo = groupRepo;
    }

    public void start() throws JsonProcessingException, InterruptedException {
        // Получаем айдишники всех факультетов универа
        long m = System.currentTimeMillis();
        final String mainPageHtml = getMainPage();
        final List<Pair<String, String>> idFacultsList = getIdFacultsFromMainPage(mainPageHtml);
        idFacultsList.forEach(facult -> {
            System.out.println(facult.getKey() + "\t\t" + facult.getValue());
        });

        // Получаем инфу по группам
        final List<GroupInfo> groupList = this.getGroupList(idFacultsList);
        System.out.println(groupList.size());

        this.saveGroupSheduleToFile(groupList);

        AtomicInteger count = new AtomicInteger();
        int allCount = groupList.size();
        AtomicInteger scheduleCount = new AtomicInteger();
        WorkQueue workQueue = new WorkQueue(4);
        Lock lockFaculty = new ReentrantLock();
        Lock lockGroup = new ReentrantLock();
        Lock lockTimeSubject = new ReentrantLock();
        Lock lockShedule = new ReentrantLock();
        Lock lockTeacherRang = new ReentrantLock();
        Lock lockTeacher = new ReentrantLock();
        Lock lockSubject = new ReentrantLock();
        Lock lockDay = new ReentrantLock();
        Lock lockCabinet = new ReentrantLock();
        Lock lockSubjectType = new ReentrantLock();

        for (GroupInfo groupInfo : groupList) {
            workQueue.execute(() -> {
                System.out.println("\ncount " + count.getAndIncrement() + "/" + allCount);
                System.out.println(groupInfo.getId());
                final String str = readFromFile("files/html_" + groupInfo.getId() + ".txt");
                final List<Shedule_parser> groupSheduleParser = getGroupShedule(groupInfo, str);
                scheduleCount.addAndGet(groupSheduleParser.size());

                lockFaculty.lock();
                Faculty faculty = facultyRepo.findFacultyByFacultyId(groupInfo.getFaculty_id());
                if (faculty == null) {
                    faculty = new Faculty();
                    faculty.setFacultyId(groupInfo.getFaculty_id());
                    faculty.setFacultyName(groupInfo.getFacult_name());
                    faculty = facultyRepo.save(faculty);
                }
                lockFaculty.unlock();
                // получаем существующую группу
                lockGroup.lock();
                Group group = groupRepo.findGroupByGroupId(groupInfo.getId());
                if (group == null) {
                    group = new Group();
                    group.setStartYear(groupInfo.getStart_year());
                    group.setName(groupInfo.getName());
                    group.setSpecialityId(groupInfo.getSpeciality_id());
                    group.setGroupBr(groupInfo.getGroup_br());
                    group.setGroupId(groupInfo.getId());
                    group.setFaculty(faculty);
                    group = groupRepo.save(group);
                }
                lockGroup.unlock();

                if (groupSheduleParser.size() != 0) {
                    for (Shedule_parser sheduleParser : groupSheduleParser) {
                        Shedule shedule = new Shedule();

                        lockTimeSubject.lock();
                        final TimeSubject timeSubject =
                                timeSubjectService.getByStartEnd(sheduleParser.getTimeStart(), sheduleParser.getTimeFinish());
                        shedule.setTimeSubject(timeSubject);
                        lockTimeSubject.unlock();

                        lockSubject.lock();
                        Subject subject = subjectService.findByName(sheduleParser.getSubject());
                        shedule.setSubject(subject);
                        lockSubject.unlock();

                        lockSubjectType.lock();
                        final SubjectType subjectType = subjectTypeService.getByName(sheduleParser.getSubject_type());
                        shedule.setSubjectType(subjectType);
                        lockSubjectType.unlock();

                        lockCabinet.lock();
                        Cabinet cabinet = cabinetService.findByTitle(sheduleParser.getCabinet());
                        shedule.setCabinet(cabinet);
                        lockCabinet.unlock();

                        lockTeacherRang.lock();
                        final TeacherRang teacherRang
                                = teacherRangService.getByRangName(sheduleParser.getTeacher_rang());
                        lockTeacherRang.unlock();

                        lockTeacher.lock();
                        final Teacher teacher =
                                teacherService.getTeacherByName(sheduleParser.getTeacher(), teacherRang);
                        shedule.setTeacher(teacher);
                        lockTeacher.unlock();

                        shedule.setWeek(sheduleParser.getWeek());

                        lockDay.lock();
                        final Day day = dayService.findByDayNameEquals(sheduleParser.getDayName());
                        shedule.setDay(day);
                        lockDay.unlock();

                        shedule.setStarYear(groupInfo.getStart_year());

                        shedule.setGroup(group);


                        lockShedule.lock();
                        shedule = sheduleRepo.save(shedule);
                        lockShedule.unlock();
                    }
                } else {

                }
                workQueue.isExecuting.remove(workQueue.isExecuting.size() - 1);
            });

        }
        int sleepCount = 0;
        while (true) {
            if (!workQueue.isQueueEmpty()) {
                Thread.sleep(1000);
                System.out.println("sleep " + sleepCount++);
            } else
                break;
        }
        workQueue.stopThreads();
        System.out.println("вышли из цикла");
        System.out.println("scheduleCount = " + scheduleCount);
        System.out.println("FULL TIME = " + String.format("%.2f", (double) (System.currentTimeMillis() - m) / 1000.0));

    }

    int counter = 0;

    /**
     * Получить расписание конкретной группы
     *
     * @param groupInfo группа
     * @return расписание группы
     */
    private List<Shedule_parser> getGroupShedule(GroupInfo groupInfo, String result) {

        List<Shedule_parser> sheduleParserList = new ArrayList<>();

//        final String result = this.executeRequest("https://www.altstu.ru/m/s/" + groupInfo.getId() + "/");
        Document doc = Jsoup.parseBodyFragment(result);
        final Element role = doc.getElementsByTag("main").get(0);
        String currentWeek = "";
        List<Pair<String, Element>> daysList = new ArrayList<>();

        // Получаем список дней с указанием номера недели
        for (Element element : role.children()) {
            if (element.tagName().equals("h4")) {
                currentWeek = element.html();
                continue;
            }
            if (element.hasClass("block-index")) {
                daysList.add(new Pair<>(currentWeek.trim().split(" ")[1], element));
            }
        }

        // достаем из дней расписание
        for (Pair<String, Element> stringElementPair : daysList) {
            final String week = stringElementPair.getKey();
            final Element element = stringElementPair.getValue();
            {
                final Element child = element.child(0);
                if (!child.tagName().equals("h2"))
                    continue;
                final String dayName = child.getElementsByTag("h2").html();
                final Elements arr = element.getElementsByClass("list-group").get(0)
                        .getElementsByClass("list-group-item");
                for (Element el : arr) {
                    String html = el.html().replace("\n", " ");
                    html = html.replaceAll("\\u2009", " ");
                    //System.out.println("==============================================");
                    //System.out.println(groupInfo.getId());
                    //System.out.println(html);


                    String time = "";
                    Matcher matcher_time = Pattern.compile("(.*?)<strong>").matcher(html);
                    if (matcher_time.find())
                        time = (matcher_time.group(1)).trim();
                    int timeStart = 0;
                    int timeFinish = 0;
                    {
                        time = time.replace(" ", "");
                        final String[] split = time.split("-");
                        timeStart = LocalTime.parse(split[0] + ":00").toSecondOfDay();
                        timeFinish = LocalTime.parse(split[1] + ":00").toSecondOfDay();

                    }
                    html = html.substring(html.indexOf(time), html.length());

                    String subject = "";
                    Matcher matcher_subject = Pattern.compile("<strong>(.*?)<\\/strong>").matcher(html);
                    if (matcher_subject.find())
                        subject = (matcher_subject.group(1)).trim();
                    html = html.substring(html.indexOf(subject), html.length());


                    String subject_type = "";
                    Matcher matcher_subject_type = Pattern.compile("<\\/strong>(.*?)<nobr>").matcher(html);
                    if (matcher_subject_type.find())
                        subject_type = (matcher_subject_type.group(1)).trim();
                    if (subject_type.length() > 0)
                        subject_type = subject_type.substring(0, subject_type.length() - 2);
                    html = html.substring(html.indexOf(subject_type), html.length());

                    if (html.contains("подгруппа")) {
                        int i = html.indexOf("</nobr>");
                        i += "</nobr>".length();
                        String firstPart = html.substring(0, i + 1);
                        String secondPart = html.substring(i + 1, html.length());
                        int nobr = firstPart.indexOf("<nobr>");
                        firstPart = firstPart.substring(0, nobr) + firstPart.substring(nobr + "<nobr>".length(), firstPart.length());
                        nobr = firstPart.indexOf("</nobr>");
                        firstPart = firstPart.substring(0, nobr) + firstPart.substring(nobr + "<nobr>".length(), firstPart.length());
                        html = firstPart + secondPart;
                    }

                    String cabinet = "";
                    Matcher matcher_cabinet = Pattern.compile("<nobr>(.*?)<\\/nobr>").matcher(html);
                    if (matcher_cabinet.find())
                        cabinet = (matcher_cabinet.group(1)).trim();
                    html = html.substring(html.indexOf(cabinet), html.length());


                    String teacher = "";
                    Matcher matcher_teacher = Pattern.compile("<nobr>(.*?)<\\/nobr>").matcher(html);
                    if (matcher_teacher.find())
                        teacher = (matcher_teacher.group(1)).trim();
                    html = html.substring(html.indexOf(teacher), html.length());

                    String teacher_rang = "";
                    Matcher matcher_teacher_rang = Pattern.compile("<\\/nobr>(.*)").matcher(html);
                    if (matcher_teacher_rang.find())
                        teacher_rang = (matcher_teacher_rang.group(1)).trim();
                    if (teacher_rang.length() > 0)
                        teacher_rang = teacher_rang.substring(2, teacher_rang.length()).trim();
                    html = html.substring(html.indexOf(teacher_rang), html.length());

                    // String subject = html.split("<strong>(.*?)<\\/strong>")[0];
                    //System.out.println(week + "\t" + dayName + "\t" + time + "\t" + subject + "\t" + subject_type + "\t" + cabinet + "\t" + teacher + "\t" + teacher_rang);
                    // коллизии, иногда не указывается кабинет, и в поле кабинет попадает имя препода
                    if (cabinet.length() > 5 && cabinet.charAt(cabinet.length() - 1) == '.' && cabinet.charAt(cabinet.length() - 4) == '.') {
                        teacher = cabinet;
                        cabinet = "";
                    }

                    Shedule_parser sheduleParser = new Shedule_parser();
                    sheduleParser.setGroupInfo(groupInfo);

                    sheduleParser.setWeek(Integer.valueOf(week));
                    sheduleParser.setDayName(dayName);
                    sheduleParser.setTime(time);
                    sheduleParser.setTimeStart(timeStart);
                    sheduleParser.setTimeFinish(timeFinish);
                    sheduleParser.setSubject(subject);
                    subject_type = subject_type.trim();
                    if (subject_type.length() > 1 && !subject_type.contains(")"))
                        subject_type += ")";
                    sheduleParser.setSubject_type(subject_type);
                    sheduleParser.setCabinet(cabinet);
                    sheduleParser.setTeacher(teacher);
                    sheduleParser.setTeacher_rang(teacher_rang);

                    sheduleParserList.add(sheduleParser);
                }
            }
        }
        return sheduleParserList;
    }

    /**
     * Получить расписания списка групп
     *
     * @param groupList список групп
     * @return список расписаний
     * @throws InterruptedException
     */
    private List<Shedule_parser> saveGroupSheduleToFile(List<GroupInfo> groupList) throws InterruptedException {
        WorkQueue workQueue = new WorkQueue(25);
        List<String> resultList = new ArrayList<>();
        List<Shedule_parser> sheduleParserList = new ArrayList<>();
        long m = System.currentTimeMillis();

        for (GroupInfo groupInfo : groupList) {
            workQueue.execute(() -> {
                String str = "";
//                System.out.print(groupInfo.getId() + " " + groupInfo.getName());
                str += groupInfo.getId() + " " + groupInfo.getName();
                String result = this.executeRequest("https://www.altstu.ru/m/s/" + groupInfo.getId());
                if (result.lastIndexOf("Занятий нет") != -1) {
                    //System.out.print("  У этой группы нет занятий\t");
                    str += "\t\t\tУ этой группы нет занятий\t";
//                    Shedule shedule = new Shedule();
//                    shedule.setGroupInfo(groupInfo);
//                    sheduleList.add(shedule);
                } else {
//                    System.out.print("  У этой группы есть занятия\t");
                    str += "\t\t\tУ этой группы есть занятия\t";
                    //final Shedule groupShedule = this.getGroupShedule(groupInfo);
                    //sheduleList.add(groupShedule);
                }
                printToFile(result, "files/html_" + groupInfo.getId() + ".txt");
                System.out.println("end " + counter++);
                resultList.add(str);
                workQueue.isExecuting.remove(workQueue.isExecuting.size() - 1);
            });

        }
        // Ждем пока все запросы не выполнятся
        int x = 0;
        while (true) {
            if (!workQueue.isQueueEmpty()) {
                //System.out.println("size = " + workQueue.isExecuting.size());
                Thread.sleep(1000);
                System.out.println("sleep " + x++);
            } else {
                //System.out.println("пусто, идем дальше");
                break;
            }
        }

        System.out.println("вышли из цикла");

        System.out.println(String.format("%.2f", (double) (System.currentTimeMillis() - m) / 1000.0));
        System.out.println();
        workQueue.stopThreads();
        for (String s : resultList) {
            System.out.println(s);
        }
        return sheduleParserList;
    }

    /**
     * Получить список групп по айдишникам факультетов
     *
     * @param idFacultsList айдишники факультетов Pair<String,String> - Pair<айдишник факультета, название факультета>
     * @return список групп
     * @throws InterruptedException
     */
    private List<GroupInfo> getGroupList(List<Pair<String, String>> idFacultsList)
            throws InterruptedException {
        WorkQueue workQueue = new WorkQueue(15);
        long m = System.currentTimeMillis();
        String baseUrl = "https://www.altstu.ru/main/schedule/ws/group/?f=";
        List<GroupInfo> resultParse = new ArrayList<>();
        for (Pair<String, String> stringStringPair : idFacultsList) {
            final String facultId = stringStringPair.getKey();
            final String facultName = stringStringPair.getValue();

            //https://www.altstu.ru/main/schedule/ws/group/?f=73


            workQueue.execute(() -> {
                final String groupsInfo = executeRequest(baseUrl + facultId);
                ObjectMapper mapper = new ObjectMapper();
                try {
                    List<GroupInfo> groupsByFacultList =
                            mapper.readValue(groupsInfo, new TypeReference<List<GroupInfo>>() {
                            });
                    groupsByFacultList.stream()
                            .peek(groupInfo -> groupInfo.setFacult_name(facultName)).collect(Collectors.toList());

                    resultParse.addAll(groupsByFacultList);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                System.out.println("end");
                workQueue.isExecuting.remove(workQueue.isExecuting.size() - 1);
            });
        }
        // Ждем пока все запросы не выполнятся
        while (true) {
            if (!workQueue.isQueueEmpty()) {
                //System.out.println("size = " + workQueue.isExecuting.size());
                Thread.sleep(100);
            } else {
                //System.out.println("пусто, идем дальше");
                break;
            }
        }

        System.out.println("вышли из цикла");

        System.out.println(String.format("%.2f", (double) (System.currentTimeMillis() - m) / 1000.0));
        System.out.println();
        workQueue.stopThreads();
        return resultParse;
    }

    /**
     * Выполнить запрос
     *
     * @param url юрл запроса
     * @return строка - содержимое ответа
     */
    private String executeRequest(String url) {
        String result = "";
        try {
            HttpClient client = new DefaultHttpClient();

            HttpGet post = new HttpGet(url);
            //post.setHeader("Referer", "http://localhost/something");
            //post.setHeader("Authorization", "Basic (with a username and password)");
            //post.setHeader("Content-type", "application/json");
            post.setHeader("x-requested-with", "XMLHttpRequest");

            HttpResponse response = null;

            response = client.execute(post);

            HttpEntity entity = response.getEntity();
            Header encodingHeader = entity.getContentEncoding();

// you need to know the encoding to parse correctly
            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 :
                    Charsets.toCharset(encodingHeader.getValue());

// use org.apache.http.util.EntityUtils to read json as string
            String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);

            result = json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Спарсить главную страницу расписаний
     *
     * @return html страница
     */
    private String getMainPage() {
        String result = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(
                    "https://www.altstu.ru/main/schedule/");
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                result += output;
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Получить список факультетов универа
     *
     * @param mainPageHtml html страница расписания
     * @return айдишники факультетов Pair<String,String> - Pair<айдишник факультета, название факультета>
     */
    private List<Pair<String, String>> getIdFacultsFromMainPage(String mainPageHtml) {
        List<Pair<String, String>> result = new ArrayList<Pair<String, String>>();
        Document doc = Jsoup.parseBodyFragment(mainPageHtml);
        final Element id_faculty = doc.getElementById("id_faculty");
        for (Element child : id_faculty.children()) {
            final String value = child.attr("value");
            final String html = child.html();
            result.add(new Pair<>(value, html));
        }
        return result;
    }

    private boolean printToFile(String str, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(str);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    private String readFromFile(String filename) {
        String result = "";
        try {
            result = Files.lines(Paths.get(filename), StandardCharsets.UTF_8)
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
