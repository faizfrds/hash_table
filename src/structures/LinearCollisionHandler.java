package structures;

import java.security.DrbgParameters.Capability;

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

      int curIndex = index;
      int count = 0;

      while (count < M){
        
        if (!activeArray[curIndex]) return curIndex;
        if (curIndex == M-1) curIndex = 0;
        
        curIndex = (curIndex + probeLength) % M;
        count++;
      }

      return -1;
   }

  /**
* Start at index and search the array linearly until the target
* is found. Then return the array index of the target. 
* Return -1 if not found.
*/
   public int search(int startIndex, K target, K[] keyArray, boolean [] activeArray, int M){
      //TODO: Implement this method.

      int curIndex = startIndex;

      while (activeArray[curIndex]) {
        
        if (keyArray[curIndex] == target) return curIndex;
        curIndex = (curIndex + 1) % M;
      }
      
    return -1;
   }
}
