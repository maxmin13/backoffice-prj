package it.maxmin.common.service.impl;

import static it.maxmin.common.constant.MessageConstants.ERROR_FIELD_NOT_EMPTY_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_FIELD_NOT_NULL_MSG;
import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.text.MessageFormat;

import org.springframework.stereotype.Service;

import it.maxmin.common.service.api.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	public String getMessage(String message, Object ... args) {
		notNull(args, ERROR_FIELD_NOT_NULL_MSG);
		notEmpty(args, ERROR_FIELD_NOT_EMPTY_MSG);
		return MessageFormat.format(message, args);
	}
}
