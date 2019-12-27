package logger;

import request.Request;
import response.Response;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private File loggerFile;
	private Request request;
	private Response response;

	public Logger(File loggerFile, Request request, Response response) {
		this.loggerFile = loggerFile;
		this.request = request;
		this.response = response;
	}

	public void write () {
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");
			Date date = new Date();
			String dateString = dateFormat.format(date);
			String loggerMessage = request.getRequestIPAdress() + " - " + "[" + dateString + "]"
					 + request.getRequestLine() + " " + response.getStatusCode() + " " + response.getBodyLength();
			FileWriter fileWriter = new FileWriter(loggerFile.getAbsoluteFile(), true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(loggerMessage + "\n");
			bufferedWriter.close();
			System.out.println(loggerMessage);
		} catch (Exception e) {
		}
	}
}