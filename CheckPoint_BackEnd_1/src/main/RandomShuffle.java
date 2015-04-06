package main;

public class RandomShuffle {
    private static int[] generateIntegerSequence( int size ) {
        int array[] = new int[size] ;
        for( int i = 0 ; i < size ; ++i )
            array[i] = i+1 ;
        return array ;
    }

    public static void randomShuffle( int[] array ) {
        int SIZE = array.length ;
        int arrayTemp[] = generateIntegerSequence(SIZE) ;
        int toShuffle[] = new int[SIZE/4] ;
        int randomIndex ;
        for(int i = 0 ; i < SIZE/4 ; ++i) {
            randomIndex = (int) Math.round(Math.random() * (SIZE - 1 - i));
            toShuffle[i] = arrayTemp[randomIndex]-1 ;
            arrayTemp[SIZE-1-i] ^= arrayTemp[randomIndex] ;
            arrayTemp[randomIndex] ^= arrayTemp[SIZE-1-i] ;
            arrayTemp[SIZE-1-i] ^= arrayTemp[randomIndex] ;
        }

        for(int i = 0 ; i < SIZE/8; ++i) {
            array[toShuffle[2*i]] ^= array[toShuffle[2*i+1]] ;
            array[toShuffle[2*i+1]] ^= array[toShuffle[2*i]] ;
            array[toShuffle[2*i]] ^= array[toShuffle[2*i+1]] ;
        }
    }
}
