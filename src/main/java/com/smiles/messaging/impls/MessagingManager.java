/**
 * 
 */
package com.smiles.messaging.impls;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smiles.messaging.interfaces.PushService;
import com.smiles.messaging.models.Configs;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.APIConnectionException;
import cn.jpush.api.common.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

/**
 * @author yaojliu
 *
 */
public class MessagingManager implements PushService{
	
	private static final JPushClient jpushClient = new JPushClient(
			Configs.JPUSH_MASTER_SECRET,Configs.JPUSH_APPKEY);
	
    private static final Logger LOG = LoggerFactory.getLogger(MessagingManager.class);
    
    private static final MessagingManager instance = new MessagingManager();

	/**
	 * 
	 */
	private MessagingManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static MessagingManager getInstance(){
		return instance;
	}

	@Override
	public PushResult notifyAllWithContent(String content) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(content)){			
			content = "hello kitty";
		}
		
		PushPayload payload = PushPayload.alertAll(content);
//		payload = payload
//                .setNotification(Notification.ios(myName + " send you a message", new HashMap<String, String>()))
//    	        .setAudience(Audience.alias("all"))
//                .setMessage(cn.jpush.api.push.model.Message.newBuilder()
//                        .setMsgContent(content)
//                        .setTitle(myName)
//                        .addExtras(new HashMap<String, String>())
//                        .build());
		
		try {
			LOG.info("payload : " + payload.toJSON());
            PushResult result = jpushClient.sendPush(payload);
            return result;
//            LOG.info("msg_id : " + result.msg_id, "sendno : " + result.sendno);          
        } catch (APIConnectionException e) {
            // TODO: need retry
            
        } catch (APIRequestException e) {
            String info = "Send msg error - errorCode:" + e.getErrorCode()
                    + ", errorMsg:" + e.getErrorMessage();
            LOG.error(info);
        }
		return null;
	}

	@Override
	public PushResult notifyOrderInit() {
		return null;
		// TODO Auto-generated method stub
		
	}

}
