package com.spring.io.hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

	@RequestMapping("/execute")
	public String execute() {

		return getExecutedValue(null);
	}

	@RequestMapping("/execute/{id}")
	public String execute(@PathVariable("id") String id) {

		return getExecutedValue(id);
	}

	@RequestMapping("/ping/{id}")
	public String executePing(@PathVariable("id") String id) {

		id = id.replace("-", ".");
		return executeCommand(id);
	}

	private TimerTask tt;

	public String getExecutedValue(String id) {

		String domainName = "google.com";

		if (id != null) {
			domainName = id;
		}

		String command = "ping -c 3 " + domainName;
		String output = executeCommand(command);
		System.out.println(output);
		return output;
	}

	private String executeCommand(String command) {
		StringBuffer buffer = new StringBuffer();
		Process p;
		try {
			String ping = "ping";
			p = Runtime.getRuntime().exec(ping +" "+command);

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			Timer t = new Timer();
			tt = new TimerTask() {
				@Override
				public void run() {

					buffer.append("Host is reachable");
					buffer.append("@@@@");
					buffer.append("Time Out");
					buffer.append("@@@@");
					p.destroy();
					cancelTimer();
				};
			};

			t.schedule(tt, 5000, 5000);

			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "@@@@");
			}

			return buffer.toString();

		} catch (Exception e) {

			String msg = e.getMessage();
			return msg;
		}

	}

	public void cancelTimer() {
		tt.cancel();
	}
}
