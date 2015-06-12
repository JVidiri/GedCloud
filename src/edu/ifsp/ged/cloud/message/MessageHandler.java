package edu.ifsp.ged.cloud.message;

import org.json.JSONException;
import org.json.JSONObject;

import edu.ifsp.ged.commons.utils.Utils.enumUserJsonMessagesTypes;


/**
 * 
 * Classe para parceamento de mensagens json recebiddas.
 * 
 * @author jvidiri
 *
 */
public class MessageHandler {
	
	/**
	 * Handle user messages, indentify its types
	 * 
	 * @param message
	 * @return
	 * @throws JSONException 
	 */
	public Message userMessageHandler(String message) throws JSONException{
		JSONObject jsonMessage = new JSONObject(message);
		Message retMess = new Message();
		String type =  jsonMessage.getString("type");
		
		if (enumUserJsonMessagesTypes.REGIST.name().equals(type)){
			retMess.setMessageType(enumUserJsonMessagesTypes.REGIST);
		}else if (enumUserJsonMessagesTypes.DELETE.name().equals(type)){
			retMess.setMessageType(enumUserJsonMessagesTypes.DELETE);
		}else if (enumUserJsonMessagesTypes.MODIFY.name().equals(type)){
			retMess.setMessageType(enumUserJsonMessagesTypes.MODIFY);
		}
		
		retMess.setMessage(jsonMessage.getString("content"));		
		return retMess;		
	}
	
	
}
