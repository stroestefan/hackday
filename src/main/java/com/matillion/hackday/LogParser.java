package com.matillion.hackday;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class LogParser
{
    public static void main(String[] args)
    {
        List<LogEntry> dates = new ArrayList<>();
        List<LogEntry> tasks = new ArrayList<>();
        List<LogEntry> queueMessages = new ArrayList<>();
//        String fileName = "/home/stefanstroe/.IntelliJIdea2019.1/config/scratches/test.txt";
//        String fileName = "/home/stefanstroe/.IntelliJIdea2019.1/config/scratches/matillionLogWithoutselects.txt";
        String fileName = "/home/stefanstroe/.IntelliJIdea2019.1/config/scratches/queue.txt";
        List<LineType> lastThingAddedTo = Arrays.asList(LineType.UNDEFINED);
        try (Stream<String> stream = Files.lines(Paths.get(fileName)))
        {
            stream.forEach(e -> fillLists(dates, tasks, queueMessages, lastThingAddedTo, e));

            dates.forEach(LogEntry::toString);
            System.out.println();
            tasks.forEach(LogEntry::toString);
            System.out.println();
            queueMessages.forEach(LogEntry::toString);
        }
        catch (Exception ignored)
        {
            System.out.println("wrong");
        }
    }

    private static void fillLists(List<LogEntry> dates, List<LogEntry> tasks, List<LogEntry> queueMessages,
                                  List<LineType> lastThingAddedTo, String line)
    {
        LineType lineType = detectLineType(line);
        if (LineType.DATE.equals(lineType))
        {
            dates.add(new LogEntry(LineType.DATE, line));
            lastThingAddedTo.set(0, LineType.DATE);
        }
        else if (LineType.TASK_THREAD.equals(lineType))
        {
            tasks.add(new LogEntry(LineType.TASK_THREAD, line));
            lastThingAddedTo.set(0, LineType.TASK_THREAD);
        }
        else if (LineType.QUEUE_THREAD.equals(lineType))
        {
            queueMessages.add(new LogEntry(LineType.QUEUE_THREAD, line));
            lastThingAddedTo.set(0, LineType.QUEUE_THREAD);
        }
        else if(LineType.SOMETHING.equals(lineType))
        {
            if (LineType.DATE.equals(lastThingAddedTo.get(0)))
            {
                dates.get(dates.size() - 1).addLine(line);
            }
            else if (LineType.TASK_THREAD.equals(lastThingAddedTo.get(0)))
            {
                tasks.get(tasks.size() - 1).addLine(line);
            }
            else if (LineType.QUEUE_THREAD.equals(lastThingAddedTo.get(0)))
            {
                queueMessages.get(queueMessages.size() - 1).addLine(line);
            }
        }
    }

    private static LineType detectLineType(String line)
    {
        if (startsWithDate(line))
        {
            return LineType.DATE;
        }
        if (startswithTaskthread(line))
        {
            return LineType.TASK_THREAD;
        }
        if (isQueueMessage(line))
        {
            return LineType.QUEUE_THREAD;
        }
        return LineType.SOMETHING;
    }

    private static boolean startsWithDate(String line)
    {
        try
        {
            String substring = line.substring(0, 24);
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .append(DateTimeFormatter.ofPattern("dd-MMM-yyy"))
                    .appendLiteral(' ')
                    .append(DateTimeFormatter.ISO_TIME)
                    .toFormatter();
            LocalTime.parse(substring, formatter);
            return true;
        }
        catch (Exception exc)
        {
            return false;
        }
    }

    private static boolean startswithTaskthread(String line)
    {
        return line.matches("\\[task-thread-\\d*].*");
    }

    private static boolean isQueueMessage(String line)
    {
        return line.matches("\\[Queue Thread].*");
    }
}
