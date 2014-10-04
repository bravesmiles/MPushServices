/**
 * 
 */
package com.smiles.messaging.interfaces;

import cn.jpush.api.push.PushResult;

/**
 * @author yaojliu
 * Define the interface of message push service
 */
public interface PushService {

	public PushResult notifyOrderInit();
	
	public PushResult notifyAllWithContent(String content);

}
