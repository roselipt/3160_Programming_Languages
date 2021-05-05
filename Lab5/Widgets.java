/**  
 * Assignment #3 for Data Structures
 *   Widgets with Linked Lists
 */

import java.util.Scanner;
import java.io.*;

public class Widgets  {
  
  public static void main(String[] args) throws IOException  {
    
    final double markUp = 1.3;  
    File fileName = new File("TransactionFile.txt");
    Scanner inputFile = new Scanner(fileName);
    String str;
    String[] tokens;
    String transactionCode;
    int amt, amtRemaining, amtSold, amtThisReceipt, leftover;
    double price, priceAfterMarkUp, sale;
    WidgetDelivery singleReceipt, whatsLeft;
    DeliveryList inventory = new DeliveryList();
    DeliveryList saleList = new DeliveryList();
    int promotionCounter = 0;
    double promotionDiscount = 0;
    double discountedSale;
    Node p;
    
    str = inputFile.nextLine();

    //  Process each line from file
    while (!str.equals("End of Data")) {
        tokens = str.split("  ");
        transactionCode = tokens[0];
        

        //  If line begins with "R" for receipt, creates a WidgetDelivery object
        //  and inserts it into inventory which is a queue implemented as 
        //  dynamic list. Also displays card read in.
        if (transactionCode.equals("R"))  {
            amt = Integer.parseInt(tokens[1]);
            price = Double.parseDouble(tokens[2]);
            System.out.printf("A receipt record was read for %d widgets at $%.2f per widget\n", amt, price);
            System.out.println();
            singleReceipt = new WidgetDelivery(amt, price);
            inventory.insert(singleReceipt);
        }

        
        //  If line begins "S" for sale, processes that sale and 
        //  produces invoice.
        else if (transactionCode.equals("S"))  {
            amt = Integer.parseInt(tokens[1]);
            System.out.println(amt + " widgets ordered");
            amtRemaining = amt;
            amtSold = 0;
            saleList = new DeliveryList();
            
            //  add an if here too?
            if (!inventory.isEmpty())  {
                singleReceipt = inventory.remove();

            
            
                //  While singleReceipt does not cover the whole order
                //  or the remaining part of order, create a list of receipts
                //  until order is filled.
                while ( (amtRemaining >= singleReceipt.getQuantity()) &&
                                                    (amtRemaining > 0) )  {
                    saleList.insert(singleReceipt);
                
//                    System.out.println(singleReceipt.getQuantity() + " place on sale list");
                    amtRemaining -= singleReceipt.getQuantity();
                    amtSold += singleReceipt.getQuantity();
                
                    if ( (amtRemaining > 0) && (!inventory.isEmpty()) )
                        singleReceipt = inventory.remove();

                    else if ((amtRemaining > 0)  && (inventory.isEmpty()) )  {
                        System.out.println("remainder of " + amtRemaining + " Widgets not available from while");
                        amtRemaining = 0;
                    }        
                }    
            

                //  If receipt record is greater than amt ordered or remaining
                //  on order, execute order and adjust inventory.
                if ( (amtRemaining > 0) && (amtRemaining < singleReceipt.getQuantity()) ) {
                    amtThisReceipt = singleReceipt.getQuantity();

                    leftover = amtThisReceipt - amtRemaining;
                    price = singleReceipt.getPrice();
                    whatsLeft = new WidgetDelivery(leftover, price);
                

                    //  This needs to be changed to insert at Rear
                    inventory.insertAtRear(whatsLeft);

                    //  Adjust singleReceipt to match order and reflect stock returned
                    //  to inventory, and add it to saleList.
                
                    singleReceipt.setQuantity(amtRemaining);
                    saleList.insert(singleReceipt);
                    amtRemaining = 0;
                    amtSold += singleReceipt.getQuantity(); 
                }
            }  // close if (!inventory.isEmpty() 
            
            //  If (inventory.isEmpty()) and no part of order can be filled
            else
                System.out.println("This is some OOS message our data won't use.");
            
            //  Produce invoice from saleList
            sale = 0;
            System.out.println(amtSold + " widgets sold");
            while (!saleList.isEmpty())  {
                singleReceipt = saleList.remove();
//                System.out.print(singleReceipt.getQuantity() + " widgets at " +
//                                singleReceipt.getPrice() + " totalling $ " +
//                                singleReceipt.getQuantity()*singleReceipt.getPrice() + " .\n");
                priceAfterMarkUp = singleReceipt.getPrice()*markUp;
                System.out.printf("%d widgets at $%.2f   Sales: $%.2f.",
                                    singleReceipt.getQuantity(),
                                    priceAfterMarkUp,
                                    singleReceipt.getQuantity()*priceAfterMarkUp);
                System.out.println();
                sale += singleReceipt.getQuantity()*priceAfterMarkUp;
            }
            System.out.printf("                 Total Sale: $%.2f\n\n", sale);
            
            //  Apply promotion if appropriate
            if (promotionCounter > 0)  {
                System.out.printf("This sale is eligible for a %.0f percent promotional discount!\n",
                                    promotionDiscount);
                discountedSale = sale*(100-promotionDiscount)*(.01);                    
                System.out.printf("         New Amount of Total Sale: $%.2f\n\n", discountedSale);
                System.out.println();
                promotionCounter --;
                
            }
                
        }  //  Close "S" for sale Else if

        
        //  If line begins "P" for promotion 
        //  set promotionCounter to 2, read amount of discount.
        else if (transactionCode.equals("P"))  {
            promotionCounter = 2;
            promotionDiscount = Double.parseDouble(tokens[1]);
            System.out.printf("A promotion card was read for %.0f percent\n", promotionDiscount);
            System.out.println();
        }

        str = inputFile.nextLine();    
    }  //  Close while loop to read file.  

    System.out.println("End of Data reached\n");

    //  Display remaining inventory.
    System.out.println("Remaining inventory:");

    p = inventory.getRear();
    while (p != null)  {
        singleReceipt = p.getInfo();
        System.out.printf("%d Widgets at $%.02f per Widget\n",
                            singleReceipt.getQuantity(),
                            singleReceipt.getPrice());
        p = p.getNext();
    }

//      System.out.println("And if my grandmother had wheels she'd be a wagon.");

  } 
//}   //close Widgets class



public class DeliveryList  {

  private Node front;
  private Node rear;
  
  public DeliveryList()  {
      front = null;
      rear = null;
      
  }
  
  public boolean isEmpty()  {
      return rear == null;
  }
  
  public void insertFirst(WidgetDelivery x)  {
      Node q = new Node(x, null);
//      if (!isEmpty())
//        q.setNext(front);
      front = q;
      rear = q;
//      System.out.println("insertFirst is called here.");
  }
  
  public void removeFirst()  {
      rear = null;
      front = null;
  }
  
  public void insertAtRear(WidgetDelivery x)  {
      Node q = new Node(x, null);
      if (isEmpty())
        insertFirst(x);
      else {
        q.setNext(rear);
        rear = q;
  }
//        System.out.println("Hello from insert at rear!");
  }

  public void insert(WidgetDelivery x)  {
      Node q = new Node(x,null);
      if (front != null)  { 
        front.setNext(q);
        front = q;
//        System.out.println("Hello from insert method (front !=null)."); 
      }
      else  
        insertFirst(x);
      
  }  

  public WidgetDelivery remove()  {
      WidgetDelivery info;
      info = rear.getInfo();
      Node p = rear.getNext();
      if (rear.getNext() == null)
        removeFirst();
      else 
        rear = p;
      return info;
  }

  public Node getRear()  {
      return rear;
  }    
}  // close of DeliveryList class


  public class Node  {
    
        private WidgetDelivery delivery;
        private Node next;
    
        public Node(WidgetDelivery x, Node n)  {
            delivery = x;
            next = n;
        }
        
        public WidgetDelivery getInfo()  {
            return delivery;    
        }
        
        public Node getNext() {
            return next;
        }
        
        public void setInfo(WidgetDelivery x)  {
            delivery = x;
        }
        
        public void setNext(Node p)  {
            next = p;
        }
    }  // close Node class





public class WidgetDelivery  {
  
    int quantity;
    double price;
    int next;
    
  public WidgetDelivery(int x, double y)  {
      quantity = x;
      price = y;
      next = -1;
  }
  
    public void setQuantity(int x)  {
      quantity = x; 
    } 
    
    public int getQuantity()  {
        return quantity;
    }
    
    public double getPrice()  {
        return price;
    }
    
    public int next()  {
        return next;
    }
}  //close WidgetDelivery

}  //close Widgets class


