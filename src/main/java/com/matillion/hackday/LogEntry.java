package com.matillion.hackday;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

//@Data
//@Embeddable
public class LogEntry
{
    private List<String> lines = new ArrayList<>();
    private LineType type;
    private Long id;
    private String date = "";
    private String logType = "";
    private String thread = "";
    private String text = "";

    public LogEntry(LineType type, String line)
    {
        this.type = type;
        lines.add(line);
    }

    public LogEntry(LineType type, String line, Long id)
    {
        this.type = type;
        this.id = id;
        lines.add(line);
    }

    void addLine(String line)
    {
        lines.add("\n" + line);
    }

    void splitIntofields()
    {
//        if (lines.get(0).sw)
//        {
//
//        }
    }

    public List<String> getLines()
    {
        return lines;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        lines.forEach(sb::append);
        System.out.println(sb.toString());
        return sb.toString();
    }
}
