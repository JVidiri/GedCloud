package edu.ifsp.ged.cloud.message;

import edu.ifsp.ged.commons.utils.Utils.enumUserJsonMessagesTypes;

/**
 * 
 * generic message for handler messageHandler return.
 * 
 * @author jvidiri
 *
 */
public class Message {

	private enumUserJsonMessagesTypes messageType;
	private String message;

	public enumUserJsonMessagesTypes getMessageType() {
		return messageType;
	}

	public void setMessageType(enumUserJsonMessagesTypes messageType) {
		this.messageType = messageType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
