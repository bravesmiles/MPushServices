package com.smiles.messaging.restful;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.jpush.api.push.PushResult;

import com.smiles.messaging.Application;
import com.smiles.messaging.impls.MessagingManager;
import com.smiles.messaging.models.Kitty;
import com.smiles.messaging.models.User;
import com.smiles.messaging.persistence.MessagesManager;

@RestController
public class MainController {

	private static final String template = "Hello, %s!";
	
//	@Autowired
//	private MessagingManager messagingManager;
	
	private ApplicationContext context = Application.getContext();
	
	private MessagingManager messagingManager = (MessagingManager) context.getBean("messagingManager");
//	private MessagingManager messagingManager = MessagingManager.getInstance();
	private MessagesManager messagesManager = MessagesManager.getInstance();
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value="/", method=RequestMethod.GET)
    public String index() {
        return "index.html";
    }

	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public Kitty helloKitty(
			@RequestParam(value = "name", required = false, defaultValue = "kitty") String name) {
		return new Kitty("hello " + name);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public User test(
			@RequestParam(value = "userId", required = false, defaultValue = "hello") String userId,
			@RequestParam(value = "alias", required = false, defaultValue = "kitty") String alias) {
		
		messagingManager.notifyAllWithContent("Hello " + alias);
		User user = messagesManager.getUserByAlias(alias);
		if (user == null) {
			user = new User(userId, alias);
			if (messagesManager.insertUser(user) > 0) {
				return user;
			}
		}
		return user;
	}
	
	@RequestMapping(value = "/notifyAll", method = RequestMethod.GET)
	public PushResult notifyAllWithContent(
			@RequestParam(value = "content", required = false, defaultValue = "hello kitty") String content){
		return messagingManager.notifyAllWithContent(content);
	}
}
