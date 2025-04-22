package net.dvdplay.task;

public interface IEvent {
   boolean isTimeToExecute();

   void execute();
}
