import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/*
 ** Skip list node: a level and an array of references to objects.
 **
 */
public class SkipListNode<ElementType extends Comparable<ElementType>>
{
    public static final int NODE_WIDTH = 9;
    private int level_;
    private ElementType data_;
    private SkipListNode<ElementType>[] forward_;
    
    // Hashtable key= level; value = count
    // Start with ten, expand by ten when limit reached.
    private Hashtable HtableCounts = new Hashtable(10,10);

    // Create an array of a (generic) type.
    @SafeVarargs
    protected static <E extends Comparable<E>> SkipListNode<E>[] createArray(int length, SkipListNode<E>... array)
    {
	return java.util.Arrays.copyOf(array, length);
    }

    // Constructor for a node with no data, 1 level.
    public SkipListNode(int l)
    {
	this(l, null);
    }

    // Constructor for a node with some data, 1 level.
    public SkipListNode(int l, ElementType key)
    {
	level_ = l;
	data_ = key;
	forward_ = createArray(l, this);
    }

    // Return number of levels for a node.
    public int levels()
    {
	return level_;
    }

    // Data in a node.
    public ElementType getData()
    {
	return data_;
    }

    // Retrieves the next node at level i after the current node.
    public SkipListNode<ElementType> forwardPtr(int i)
    {
	return forward_[i-1];
    }

    // Sets the next node at level i after the current node.
    public void setForwardPtr(int i, SkipListNode<ElementType> node)
    {
	forward_[i-1] = node;
    }

    // Fills out the hash table with the correct counts.
    public void setCounts(){
    	for (int i = level_; i>0 ; i--) {
		
    	    // Count is the rank difference to the reference node on each level it exists on.
    	    int count = 0;
    	    SkipListNode<ElementType> nextNodeThisLevel = forwardPtr(i);
    	    SkipListNode<ElementType> nextNodeFirstLevel = forwardPtr(1);
    	   
    	    // Goes through the bottom row until it finds reference node.
    	    // Since this is a skip list, items on the right of the current node is always bigger for every level.
    	    // Items on top level must also exist on the bottom level.
    	    while (nextNodeThisLevel != nextNodeFirstLevel) { 
    	    	nextNodeFirstLevel = nextNodeFirstLevel.forwardPtr(1);
    	    	count ++;
    	    	}
				
	    	// Adds count for each level to the hash table.
	    	HtableCounts.put(i,count);
    	}
    }
    
    // Helper for the toString() method.
    private void oneline(StringBuffer sbuffer, int len, char start, char middle, String end)
    {
	sbuffer.append(start);
	for (int i = 0; i < len - 2; i++)
	{
	    sbuffer.append(middle);
	}
	sbuffer.append(end);
    }

    protected void drawArrow(StringBuffer sbuffer, int firstLevel, int lastLevel, int nextLevels, char middle, char end)
    {
	for (int lev = firstLevel; lev <= lastLevel; lev++)
	{
	    for (int j = 0; j < (NODE_WIDTH-1)/2; j++)
	    {
		sbuffer.append(' ');
	    }
	    sbuffer.append(lev <= nextLevels ? end : middle);

	    for (int j = 0; j < NODE_WIDTH/2; j++)
	    {
		sbuffer.append(' ');
	    }
	}
	sbuffer.append('\n');
    }

    protected String toString(int maxLevel)
    {
	StringBuffer sbuffer = new StringBuffer();

	// Top row followed by the arrows that bypass node.
	int len = level_ * NODE_WIDTH;
	oneline(sbuffer, len, '+', '-', "+");
	if (maxLevel > 0)
	{
	    drawArrow(sbuffer, level_ + 1, maxLevel, 0, '|', '|');
	}

	// Second row followed by the arrows that bypass node.
	oneline(sbuffer, len, '|', ' ', "|");
	if (maxLevel > 0)
	{
	    drawArrow(sbuffer, level_ + 1, maxLevel, 0, '|', '|');
	}

	// Center the data item within the node.
	String label = data_.toString();
	int left = (len - label.length())/2;
	oneline(sbuffer, left, '|', ' ', " ");
	sbuffer.append(label);
	oneline(sbuffer, len - left - label.length(), ' ', ' ', "|");
	if (maxLevel > 0)
	{
	    drawArrow(sbuffer, level_ + 1, maxLevel, 0, '|', '|');
	}

	// Fourth row followed by the arrows that bypass node.
	oneline(sbuffer, len, '|', ' ', "|");
	if (maxLevel > 0)
	{
	    drawArrow(sbuffer, level_ + 1, maxLevel, 0, '|', '|');
	}

	// Bottom row followed by the arrows that bypass node.
	oneline(sbuffer, len, '+', '-', "+");
	if (maxLevel > 0)
	{
	    drawArrow(sbuffer, level_ + 1, maxLevel, 0, '|', '|');
	}

	// Arrows that follow after node (first two rows only).
	drawArrow(sbuffer, 1, maxLevel, maxLevel, '|', '|');
	drawArrow(sbuffer, 1, maxLevel, maxLevel, '|', '|');
	return sbuffer.toString();
    }
	
	// String reprsentation.
    @Override
    public String toString()
    {
	return toString(0);
    }
}
