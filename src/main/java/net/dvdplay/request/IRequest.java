package net.dvdplay.request;

import net.dvdplay.exception.DvdplayException;

public interface IRequest {
   String NAME_RESULT = "RESULT";
   String NAME_RESULT_MESSAGE = "RESULT_MESSAGE";
   int NAME_RESULT_IX = 0;
   int NAME_RESULT_MESSAGE_IX = 1;
   String RESULT_SUCCESS = "0";
   String RESULT_FAILURE = String.valueOf(1002);

   String getRequestName() throws DvdplayException;

   String getSenderType() throws DvdplayException;

   int getSenderId() throws DvdplayException;

   String getVersion() throws DvdplayException;

   String getAsXmlString() throws DvdplayException;
}
