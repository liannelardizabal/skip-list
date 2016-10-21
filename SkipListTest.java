import java.util.Random;

/*
 **
 ** Main program: perform operations on a skip list, and count comp
 **
 */
public class SkipListTest
{
    private final static int NumberOfOperations = 10;
    private final static String usage = "***Usage: java SkipListTest <noElements>";
    
    //test 
    public static String[] test = {"7"};
    //test 
    
    
    /*
     ** Main program.
     */
    public static void main(String[] args)
    {
        Random rng = new Random();
        
        //test 
        args = test;
        //test 
        
        
        /*
         ** Check the arguments.
         */
        if (args.length != 1)
        {
            System.out.println(usage);
            return;
        }

        int noElements;

        try
        {
            noElements = Integer.parseInt(args[0]);
        }
        catch (java.lang.NumberFormatException e)
        {
            System.out.println(usage);
            return;
        }

        if (noElements <= 0)
        {
            System.out.println(usage);
            return;
        }

        /*
         ** Declarations.
         */
        boolean[]  inOut = new boolean[noElements * 2];
        SkipList<Integer> skiplist = new SkipList<Integer>();

        /*
         ** Create an array with the proper size and initialize it and the skip list.
         */
        for (int i = 0; i < noElements; i++)
        {
            inOut[2*i] = false;
            inOut[2*i+1] = true;

            skiplist.insertKey(new  Integer(2*i+1));
        }

        /*
         ** Now do a large number of random operations.
         */
        for (int i = 0; i < NumberOfOperations; i++)
        {

            /*
             ** Pick a random element.
             */
            int idx = Math.abs(rng.nextInt()) % (2 * noElements);
            Integer myInt = new Integer(idx);

            /*
             ** Insert it or delete if as appropriate.
             */
            if (inOut[idx])
            {
                skiplist.deleteKey(myInt);
            }
            else
            {
                skiplist.insertKey(myInt);
            }

            /*
             ** Do the opposite operation the next time.
             */
            inOut[idx] = !inOut[idx];
        }

        /*
         ** Print the final skip list.
         */
        System.out.println(skiplist);
    }
}
