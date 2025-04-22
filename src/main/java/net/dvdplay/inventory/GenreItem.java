package net.dvdplay.inventory;

public class GenreItem {
   private String mGenreName;
   private int mGenreId;
   private int mPriority;

   public GenreItem() {
      this.mGenreName = "";
      this.mGenreId = 0;
      this.mPriority = 0;
   }

   public GenreItem(String aGenreName, int aGenreId, int aPriority) {
      this.mGenreName = aGenreName;
      this.mGenreId = aGenreId;
      this.mPriority = aPriority;
   }

   public String getGenreName() {
      return this.mGenreName;
   }

   public void setGenreName(String aGenreName) {
      this.mGenreName = aGenreName;
   }

   public int getGenreId() {
      return this.mGenreId;
   }

   public void setGenreId(int aGenreId) {
      this.mGenreId = aGenreId;
   }

   public int getPriority() {
      return this.mPriority;
   }

   public void setPriority(int aPriority) {
      this.mPriority = aPriority;
   }

   public int compareTo(GenreItem a) {
      if (this.mPriority < a.mPriority) {
         return -1;
      } else if (this.mPriority > a.mPriority) {
         return 1;
      } else if (this.mGenreName.compareTo(a.mGenreName) < 0) {
         return -1;
      } else {
         return this.mGenreName.compareTo(a.mGenreName) > 0 ? 1 : 0;
      }
   }
}
