import java.util.Random;

/*
 ** 	A skip list node with the following methods:
 **     1) Generate a  random level.
 **     2) Search for a value in the list.
 **     3) Insert a value in the list.
 **     4) Delete a value from the list.
 **     5) Find ith smallest element.
 */

public class SkipList<EType extends Comparable<EType>> extends SkipListNode<EType>
{
	private static final int MAX_LEVEL = 32;
	private int maxLevel_;
	private int noElements_;
	private final Random rng_;
	private final SkipListNode<EType>[] searchTrail_;

	// Constructor
	public SkipList()
	{
		super(MAX_LEVEL);

		maxLevel_ = 1;
		noElements_ = 0;
		rng_ = new Random();
		searchTrail_ = SkipListNode.createArray(MAX_LEVEL, this);

		for (int i = 1; i <= MAX_LEVEL; i++)
		{
			setForwardPtr(i, null);
		}
	}

	// Return a skip list in the form of a string.
	@Override
	public String toString()
	{
		StringBuffer sbuffer = new StringBuffer();

		SkipListNode<EType> node = this.forwardPtr(1);

		drawArrow(sbuffer, 1, maxLevel_, maxLevel_, '|', '|');
		drawArrow(sbuffer, 1, maxLevel_, maxLevel_, '|', '|');

		int nextLevels = (node == null) ? maxLevel_ : node.levels();
		drawArrow(sbuffer, 1, maxLevel_, nextLevels, '|', 'V');

		while (node != null)
		{
			sbuffer.append(node.toString(maxLevel_));
			node = node.forwardPtr(1);

			nextLevels = (node == null) ? maxLevel_ : node.levels();
			drawArrow(sbuffer, 1, maxLevel_, nextLevels, '|', 'V');
		}

		return sbuffer.toString();
	}

	// Generate a random level based on probability distribution.

	private int randomLevel()
	{
		int level = 1;

		while (rng_.nextFloat() < 0.5 && level < MAX_LEVEL) level++;
		return level;
	}

	// Search for a key within the skip list by keeping track of all the nodes we followed instead of continuing forward.
	private void buildSearchTrail(EType key)
	{
		SkipListNode<EType> current = this;
		SkipListNode<EType> next;
		boolean moreLevel;

		for (int level = maxLevel_; level > 0; level--)
		{
			do
			{
				next = current.forwardPtr(level);
				moreLevel = next != null && key.compareTo(next.getData()) > 0;
				if (moreLevel)
				{
					current = next;
				}
			}
			while (moreLevel);

			searchTrail_[level-1] = current;
		}
	}

	// Determine if a key was found.
	private SkipListNode<EType> found(EType key)
	{
		buildSearchTrail(key);
		SkipListNode<EType> candidateNode = searchTrail_[0].forwardPtr(1);
		return (candidateNode != null && key.equals(candidateNode.getData())) ? candidateNode : null;
	}

	// Search for key.
	public EType searchKey(EType key)
	{
		SkipListNode<EType> candidateNode = found(key);
		return (candidateNode != null) ? candidateNode.getData() : null;
	}

	// Delete key.
	public void deleteKey(EType key)
	{
		SkipListNode<EType> nodeToDelete = found(key);

		if (nodeToDelete != null)
		{

			for (int level = nodeToDelete.levels(); level >= 1; level--)
			{
				SkipListNode<EType> start = searchTrail_[level-1];
				SkipListNode<EType> end = nodeToDelete.forwardPtr(level);

				if (start == this && end == null)
				{
					maxLevel_--;
				}
				start.setForwardPtr(level, end);
			}

			// Update element count.
			noElements_--;

			// Recompute count after deletion.
			setCounts();

		}
	}

	// Insert new key.
	public void insertKey(EType key)
	{
		if (found(key) == null)
		{

			// Create a new list.
			noElements_++;
			SkipListNode<EType> newNode = new SkipListNode<EType>(randomLevel(), key);

			// Fix list.
			for (int level = newNode.levels(); level > maxLevel_; level--)
			{
				setForwardPtr(level, newNode);
				newNode.setForwardPtr(level, null);
			}

			for (int level = Math.min(maxLevel_, newNode.levels()); level >= 1; level--)
			{
				newNode.setForwardPtr(level, searchTrail_[level-1].forwardPtr(level));
				searchTrail_[level-1].setForwardPtr(level, newNode);
			}
			maxLevel_ = Math.max(maxLevel_, newNode.levels());
		}
		// Recompute count after deletion.
		setCounts();
	}

	// Input: Integer i represents the desired ith smallest element.
	// Output: Reference to node of the ith smallest node.
	// Description: Finds the ith smallest node in the skip list.
	//				The ith smallest node in the skip list is at level i.
	
	public EType select (int i)
	{
		EType nodeAtI = null;
		SkipListNode<EType> skipList = new SkipListNode<EType>(i, nodeAtI);

		// If at level i (key from the hash table), return the node.
		if (skipList.hashCode() == i) {
			return nodeAtI;
		}

		else 
			return null;

	}
}
