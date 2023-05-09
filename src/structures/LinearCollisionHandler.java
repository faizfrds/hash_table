package structures;

import java.security.DrbgParameters.Capability;

import javax.xml.stream.events.StartElement;

public class LinearCollisionHandler <K> implements CollisionHandler <K>{
    private int probeLength;

    /**
  * Constructors to set probeLength to 1, or a different length.
  */
    public LinearCollisionHandler(){
        this.probeLength = 1;
    }

    public LinearCollisionHandler(int probeLength){
        this.probeLength = probeLength;
    }

/**
  * Method starts at index and searches linearly until an open spot
  * is found in the array. This could include index itself.
  * index = (index + probeLength) % size
  */
   public int probe(int index, boolean[] activeArray, int M) {
      //TODO: Implement this method.

      if (!activeArray[index]) {
        return index;
      } else {
        int nextIndex = (index + probeLength) % M;
        return probe(nextIndex, activeArray, M);
      }
   }

  /**
* Start at index and search the array linearly until the target
* is found. Then return the array index of the target. 
* Return -1 if not found.
*/
   public int search(int startIndex, K target, K[] keyArray, boolean [] activeArray, int M){
      //TODO: Implement this method.

      if (!activeArray[startIndex] && keyArray[startIndex] == null) return -1;

      if (activeArray[startIndex]){
        if (keyArray[startIndex] == target) return startIndex;
      }
      
      return search((startIndex + probeLength) % M, target, keyArray, activeArray, M);
   }
}