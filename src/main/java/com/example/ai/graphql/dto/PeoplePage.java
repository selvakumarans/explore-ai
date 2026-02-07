import java.util.List;

public class PeoplePage<T> {
   private List<T> content;
   private int totalPages;
   private long totalElements;
   private boolean last;
   private int size;
   private int number;
   private boolean first;
   private int numberOfElements;

   // Getters and Setters
   public List<T> getContent() {
       return content;
   }

   public void setContent(List<T> content) {
       this.content = content;
   }

   public int getTotalPages() {
       return totalPages;
   }

   public void setTotalPages(int totalPages) {
       this.totalPages = totalPages;
   }

   public long getTotalElements() {
       return totalElements;
   }

   public void setTotalElements(long totalElements) {
       this.totalElements = totalElements;
   }

   public boolean isLast() {
       return last;
   }

   public void setLast(boolean last) {
       this.last = last;
   }

   public int getSize() {
       return size;
   }

   public void setSize(int size) {
       this.size = size;
   }

   public int getNumber() {
       return number;
   }

   public void setNumber(int number) {
       this.number = number;
   }

   public boolean isFirst() {
       return first;
   }

   public void setFirst(boolean first) {
       this.first = first;
   }

   public int getNumberOfElements() {
       return numberOfElements;
   }

   public void setNumberOfElements(int numberOfElements) {
       this.numberOfElements = numberOfElements;
   }
}