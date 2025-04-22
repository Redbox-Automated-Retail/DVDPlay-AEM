package net.dvdplay.util;

public class StopWatch {
   private long startTime = -1L;
   private long stopTime = -1L;
   private boolean running = false;

   public StopWatch start() {
      this.startTime = System.currentTimeMillis();
      this.running = true;
      return this;
   }

   public StopWatch stop() {
      this.stopTime = System.currentTimeMillis();
      this.running = false;
      return this;
   }

   public long getElapsedTime() {
      if (this.startTime == -1L) {
         return 0L;
      } else {
         return this.running ? System.currentTimeMillis() - this.startTime : this.stopTime - this.startTime;
      }
   }

   public StopWatch reset() {
      this.startTime = -1L;
      this.stopTime = -1L;
      this.running = false;
      return this;
   }
}
