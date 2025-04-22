package net.dvdplay.task;

import java.lang.reflect.Method;
import net.dvdplay.logger.Log;

public class ExecuteThread extends Thread {
   private boolean mDone = false;
   private Method mMethod;
   private Object[] mArgs;
   private Object mObj;
   private Object mReturnValue;
   private Exception mE;

   public boolean isDone() {
      return this.mDone;
   }

   public void setDone() {
      this.mDone = true;
   }

   public Object getReturnValue() {
      return this.mReturnValue;
   }

   public Exception getException() {
      return this.mE;
   }

   public boolean isException() {
      return this.mE != null;
   }

   public ExecuteThread(Object aObj, Method aMethod, Object[] aArgs) {
      this.mMethod = aMethod;
      this.mArgs = aArgs;
      this.mObj = aObj;
      this.mReturnValue = null;
      this.mE = null;
   }

   public void run() {
      try {
         this.mReturnValue = this.mMethod.invoke(this.mObj, this.mArgs);
         this.mDone = true;
      } catch (Exception var2) {
         Log.error(var2, var2.getMessage());
         this.mE = var2;
         this.mDone = true;
      }
   }
}
