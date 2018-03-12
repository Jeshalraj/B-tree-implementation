import java.util.*;
import java.lang.*;
import java.io.*;

public class BtreeNode {
			int maxdegree;							//order of the tree
			int size;
			BtreeNode parent,previous,next;			// pointers for DLL and hierarchy
			ArrayList<Float> KeyVal;				//Index key storing
			ArrayList<KeyValuePair> al;				// Leaf node key value pair storing
			ArrayList<BtreeNode> child;				//Storing child pointer at every node	
			BtreeNode(int m,BtreeNode Node)			//Constructor to Initialize the Node		
			{
				maxdegree = m;
				al = new ArrayList<KeyValuePair>();
				child = new ArrayList<BtreeNode>();
				parent= Node;
				KeyVal= new ArrayList<Float>();
			}
			
			
			
			public void add(float Key,String Value,BtreeNode Node)		//add function that inserts a new key value pair to tree
			{
				if(Node.child.isEmpty())								// If the Node has no child i.e. it is a leaf	
				{
						//System.out.println("Key Value is = " + Key);
						KeyValuePair kvp = new KeyValuePair(Key,Value);
						Node.al.add(kvp);
				
						Collections.sort(Node.al,new Comparator<KeyValuePair>(){
						public int compare(KeyValuePair k1,KeyValuePair k2)
						{
							if(k1.key > k2.key) return 1;
							if(k1.key < k2.key) return -1;
							return 0;
						}
						});
						
						if(Node.al.size()==Node.maxdegree)
						{ 
							divide(Node);							// The node is full, split it
						}
					
				}
				else													// If the node is not leaf	
				{
				
					int i=0;
					while(i<Node.al.size())
					{
						KeyValuePair k1 = Node.al.get(i);
						if(Key < k1.key) break;
						i++;
					}
					add(Key,Value,Node.child.get(i));
					
				}
				
			}
			public static void divide(BtreeNode Node)				// The function to split the nodes 
			{
				if(Node.parent==null && Node.child.isEmpty()) splitNode(Node,0); //Base case when root is divided into two parts and root is a leaf node too.
				else if(Node.parent==null && !Node.child.isEmpty()) splitNode(Node,1); //Case when root is divided and root is not leaf.
				else if(Node.parent!=null && !Node.child.isEmpty()) splitNode(Node,2);//Case when Internal Node is full.
				else splitNode(Node,3); //Case when Leaf Node is full and it is not root.	
			}
			
			
			public static void splitNode(BtreeNode Node,int flag)	// implement the splitting of nodes called from divide
			{
				int Middle = Node.al.size()/2;
				int size = Node.al.size();
				BtreeNode[] child = new BtreeNode[2];
				if(flag==0)											// splitting for Base case
				{
						child[0] = new BtreeNode(Node.maxdegree,Node);
						int i=0;
						while(i<Middle)
						{
							KeyValuePair k1 = Node.al.remove(0);
							child[0].add(k1.key,k1.value,child[0]);
							i++;
						}
						
						child[1] = new BtreeNode(Node.maxdegree,Node);
						while(i<size)
						{
							KeyValuePair k2;
							if(i==Middle)
							{
								k2 = Node.al.get(0);
								Node.KeyVal.add(k2.key);
							}
							else k2 = Node.al.remove(0);
							child[1].add(k2.key,k2.value,child[1]);
							i++;
						}
						child[0].next= child[1];
						child[0].previous = null;
						child[1].next = null;
						child[1].previous = child[0];
						Node.child.add(0,child[0]);
						Node.child.add(1,child[1]);
						
						//return child;
				}
				else if(flag==1)								//splitting for Case 1 in divide
				{
					Middle = Node.al.size()/2;
					size = Node.al.size();
					int i=0,j=0;
					child[0]= new BtreeNode(Node.maxdegree,Node);
					while(i<Middle)
					{
						KeyValuePair k1 = Node.al.remove(0);
						child[0].add(k1.key,k1.value,child[0]);
						child[0].KeyVal.add(k1.key);
						i++;
					}
					while(j<=Middle)
					{
						BtreeNode ChildNode = Node.child.get(0);
						child[0].child.add(ChildNode);
						ChildNode.parent = child[0];
						Node.child.remove(0);
						j++;
					}
					
					child[1] = new BtreeNode(Node.maxdegree,Node);
					while(Node.al.size()!=1)
					{
						KeyValuePair k2 = Node.al.remove(1);
						child[1].add(k2.key,k2.value,child[1]);
						child[1].KeyVal.add(k2.key);
						i++;
					}
					Node.KeyVal.add(Node.al.get(0).key);
					while(j<=size)
					{
						BtreeNode ChildNode = Node.child.get(0);
						child[1].child.add(ChildNode);
						ChildNode.parent= child[1];
						Node.child.remove(0);
						j++;
					}
					Node.child.add(child[0]);
					child[0].parent = Node;
					child[1].parent = Node;
					Node.child.add(child[1]);
					//return child;
				}
				else if(flag==2)						//splitting for case 2 in divide
				{
					BtreeNode Parent = Node.parent;
					int index = Parent.child.indexOf(Node);
					Middle = Node.al.size()/2;
					size = Node.al.size();
					child[0] = new BtreeNode(Node.maxdegree,Parent);
					Node.parent.child.add(index,child[0]);
					int i=0,j=0;
					while(i<Middle)
					{
						KeyValuePair k1 = Node.al.remove(0);
						child[0].add(k1.key,k1.value,child[0]);
						i++;
					}
					while(j<=Middle)
					{
						BtreeNode ChildNode = Node.child.get(0);
						child[0].child.add(ChildNode);
						ChildNode.parent = child[0];
						Node.child.remove(0);
						j++;
					}
					KeyValuePair k2 = Node.al.remove(0);
					Node.parent.al.add(index,k2);
					child[1] = new BtreeNode(Node.maxdegree,Parent);
					try
					{
						Node.parent.child.add(index+1,child[1]);
					}catch(Exception e){ Node.parent.child.add(child[1]);}

					while(Node.al.size()!=0)
					{
						KeyValuePair k3 = Node.al.remove(0);
						//System.out.println(alo.key);
						child[1].add(k3.key,k3.value,child[1]);
						i++;
					}
					while(j<=size)
					{
						BtreeNode ChildNode = Node.child.get(0);
						child[1].child.add(ChildNode);
						ChildNode.parent= child[1];
						Node.child.remove(0);
						j++;
					}
					
					
					Parent.child.remove(Node);
					if(Parent.al.size()==Parent.maxdegree) divide(Parent);
					//return child;
				}
				else													// Splitting for the last case in Divide
				{
					BtreeNode Parent = Node.parent;
					BtreeNode previous = Node.previous;
					BtreeNode next = Node.next;
					int index = Parent.child.indexOf(Node);
					Middle = Node.al.size()/2;
					size = Node.al.size()/2;
					child[0] = new BtreeNode(Node.maxdegree,Parent);
					Parent.child.add(index,child[0]);
					int i=0;
					while(i<Middle)
					{
						KeyValuePair k1= Node.al.remove(0);
						child[0].add(k1.key,k1.value,child[0]);
						i++;
					}
					Parent.al.add(index,Node.al.get(0));
					child[1] = new BtreeNode(Node.maxdegree,Parent);
					try{
						Parent.child.add(index+1,child[1]);
					}catch(Exception e){ Parent.child.add(child[1]);}
					
					while(Node.al.size()!=0)
					{
						KeyValuePair k2 = Node.al.remove(0);
						//System.out.println(alo.key);
						child[1].add(k2.key,k2.value,child[1]);
						i++;
					}
					if(previous!=null) previous.next= child[0];
					child[0].previous = previous;
					child[0].next = child[1];
					child[1].previous = child[0];
					child[1].next= next;
					if(next!=null) next.previous=child[1];
					//System.out.println("Yoo" + next.al.get(0).key);
					Parent.child.remove(Node);
					if(Parent.al.size() == Parent.maxdegree)
					{
						divide(Parent);
					}
					//return child;
				}
						
			
			}
			
			public static BtreeNode findNode(BtreeNode Node,float Key)			//Return the leaf node suitable for a Key after searching through the key
			{
				while(!Node.child.isEmpty())
				{
					ArrayList<KeyValuePair> al = Node.al;
					int i=0,lastIndex=0;
					KeyValuePair kvp = al.get(0);
					while(i<al.size() && Key >= al.get(i).key)
					{
						i++;
					}
					Node = Node.child.get(i);
				}
				return Node;
			}
			
			public static ArrayList<KeyValuePair> findRange(BtreeNode Node,float Key1,float Key2)	//Return the key value pairs for the range search
			{
				ArrayList<KeyValuePair> al = new ArrayList<KeyValuePair>();
				int count = 0;
				//System.out.println("In FInd Range = " + Key1 + "=" + Key2);
				BtreeNode First = findNode(Node,Key1);   //FInding FIrst Node that contains the Key
				BtreeNode Second = findNode(Node,Key2);  //Finding Second Node that contains the Key
				if(First.previous!=null) First = First.previous;
				if(Second.next!=null) Second = Second.next;
				while(First != Second.next)
				{
					ArrayList<KeyValuePair> al1= First.al;
					int i=0;
					while(i<al1.size())
					{
						//System.out.println(al1.get(i).key);
						if(al1.get(i).key >= Key1 && al1.get(i).key <= Key2)
						{
							KeyValuePair kvp = new KeyValuePair(al1.get(i).key,al1.get(i).value);
							al.add(kvp);
							//System.out.println("SKey = " + al1.get(i).key + "," + "Value = " + al1.get(i).value);
							count++;
						}
						i++;
					}
					First = First.next;
				}
				//System.out.println(count);
				return al;
			}
			
}
