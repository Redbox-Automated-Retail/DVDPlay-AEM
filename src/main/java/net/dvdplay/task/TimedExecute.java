package net.dvdplay.task;

import java.lang.reflect.Method;
import net.dvdplay.exception.DvdplayException;
import net.dvdplay.logger.Log;
import net.dvdplay.util.Util;

public class TimedExecute {
   public static Object execute(Object aObject, String aFunction, Class[] aParams, Object[] aArgs, int aTimeOut) {
      try {
         Method lMethod;
         if (aObject instanceof Class) {
            lMethod = ((Class)aObject).getMethod(aFunction, aParams);
         } else {
            lMethod = aObject.getClass().getMethod(aFunction, aParams);
         }

         ExecuteThread a = new ExecuteThread(aObject, lMethod, aArgs);
         a.start();
         long lNow = System.currentTimeMillis();

         for (long lStart = lNow; !a.isDone() && lNow - lStart < aTimeOut; lNow = System.currentTimeMillis()) {
            Util.sleep(100);
         }

         if (!a.isDone()) {
            a.interrupt();
            ExecuteThread var12 = null;
            throw new TimeOutException(aObject.getClass().getName() + "." + aFunction + " timed out");
         } else if (a.isException()) {
            throw new DvdplayException(aObject.getClass().getName() + "." + aFunction + " threw an exception");
         } else {
            return a.getReturnValue();
         }
      } catch (NoSuchMethodException var11) {
         Log.error(var11, "NoSuchMethodException caught: " + var11.getMessage());
         throw new DvdplayException(aObject.getClass().getName() + "." + aFunction + " failed");
      }
   }
}
