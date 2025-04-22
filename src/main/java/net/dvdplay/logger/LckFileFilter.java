package net.dvdplay.logger;

import java.io.File;
import java.io.FilenameFilter;

public class LckFileFilter implements FilenameFilter {
   public boolean accept(File dir, String name) {
      return name != null && !name.equals("") && name.endsWith(".lck");
   }
}
