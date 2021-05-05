//  Homework 4.2 Paradigms in C++
//  Tom Budny
//
//  This assignment requires the creation of a base class for records for all items
//  from which two derived classes will be created for books and cds.
//  The base class holds title, price and rating
//  The derived classes will have an extra field:
//  Chapter lengths for books
//  Track times for cds

/*
  Output from HW spec:
My book's title is: SampleBook
My CD's title is: SampleCD
Do they have the same rating? No

Their details in [title, price, rating, total pages/minutes]:
Book[ SampleBook, 9.99, A, 142 ]
CD[ SampleCD, 13.99, B, 72.8 ]
*/

#include <iostream>
#include <vector>
#include <string>
#include <numeric>
#include <array>

using namespace std;

//  ItemRecord is an abstract class which holds
//  name, price and rating for items

class ItemRecord {
    string n;       //  name
    double p;       //  price
    char r;         //  rating
public:
    ItemRecord(string s, double d, char c) {    // name, price, rating
        n = s;
        //  invariant check : price must be positive
        if (d >=0) p = d;
        else throw range_error("Negative price not allowed");
        //  invariant check : rating must be A, B, C or D
        if ( (c == 'A') || (c == 'B') || (c == 'C') || (c == 'D') ) r = c;
        else throw range_error("Rating not recognized.");
    }
    
    string name() const {return n;}     //  getter for name
    double price() const {return p;}    //  getter for price
    char rating() const {return r;} //  getter for rating

    //  overload the << operator for ItemRecord and its derived classes
    friend ostream& operator<< (ostream& os, const ItemRecord& ir) {
        os << ir.n;                     //  << override returns name only from record
        return os;
    }

    //  overload the == operator for ItemRecord and its derived classes
    //  == for any two records will compare their ratings and return boolean
    friend bool operator== (const ItemRecord& lft, const ItemRecord& rgt) {
                             
        return ( lft.rating() == rgt.rating() );
    }

    //  allow change of price from derived classes
    void set_price (double new_p) {
        p = new_p;
    }

    virtual string to_string() const = 0;   //  pure virtual declaration, if I have my terms right
                                            //  const means the function makes/allows no modifications
                                            //  =0 means "pure virtual" in the sense of requires override
                                            //  from any derived class
};

//  BookRecord is derived from ItemRecord
//  It uses ItemRecord's default constructor
//  But adds a vector of ints to hold chapter lengths
//  And a to_string function which returns all information for a BookRecord.
class BookRecord : public ItemRecord {
    vector<int> c;  //  c is vector of chapter lengths in pages (c for contents)
public:
    //  Constructor
    BookRecord(string s, double d, char r) : ItemRecord(s, d, r) {}

    ~BookRecord () {  // destructor
         //delete[]] c;    //  delete each element of vector chs
                                        //  I think maybe this doesn't need to be
                                        //  explicit for fundamental types.
                                        //  But I left it like the book does in Smiley.
    }

void add_contents(int contents) {
    c.push_back( contents);
}

void add_contents(const vector<int>& v) {
    for (auto pg : v) c.push_back(pg);
}

int pages() const {
    return accumulate(c.begin(), c.end(), 0);
}

void mark_down(double amt) {
    this->set_price(this->price() - amt);
}
//  to_string function required by virtual declaration in ItemRecord
string to_string() const {
    string whole_record = "Book[ " + this->name() + ", " + std::to_string(this->price()) + 
                            ", " + this->rating() + ", " + std::to_string(this->pages()) + " ]";
    return whole_record;
}
};

//  CDRecord is derived from ItemRecord
//  It uses ItemRecord's default constructor
//  But adds a vector of doubles to hold track lengths
//  And a to_string function which returns all information for a CDRecord.
class CDRecord : public ItemRecord {
    vector<double> c;  //  t is vector of track lengths in minutes (t for tracks)
public:
    //  Constructor
    CDRecord(string s, double d, char r) : ItemRecord(s, d, r) {}

    ~CDRecord () {  // destructor
         //delete *c;    //  delete each element of vector chs
                                        //  I think maybe this doesn't need to be
                                        //  explicit for fundamental types.
                                        //  But I left it like the book does in Smiley.
    }

void add_tracks(int tr) {
    c.push_back(tr);
}

void add_tracks(const vector<double>& v) {
    for (auto tr : v) c.push_back(tr);
}

double playtime() const {
    return accumulate(c.begin(), c.end(), 0.0);
}

void mark_down(double amt) {
    this->set_price(this->price() - amt);
}
//  to_string function to override virtual declaration in ItemRecord
string to_string() const {
    string whole_record = "CD[ " + this->name() + ", " + std::to_string(this->price()) + 
                            ", " + this->rating() + ", " + std::to_string(this->playtime()) + " ]";
    return whole_record;
}
};

int main () {

    try {
        BookRecord book("Stroustrup Loves Mentioning 1984!", 10.99, 'A');
        CDRecord cd("SampleC++D", 14.99, 'B');
        
        //  This line prints title only with overloaded << operator
        cout << endl << "My book's title is: " << book << endl;
        cout << "My CD's title is: " << cd << endl;
        cout << "Do they have the same rating? ";

        //  Totally tring to show off because the ternary if makes it look like c to me
        cout << ( (book == cd) ? "Y": "N") << endl;
        //  This code adds table of contents info to book
        vector<int> contents = {2, 20, 30, 40, 30, 20};
        book.add_contents(contents);     //  need to go back to make this accepts initializer list
        //  And the length of each track in minutes here
        vector<double> tracks {4.5, 15.0, 23.5, 4.3, 5.2, 20.3};
        cd.add_tracks(tracks);
                
        //  Decrease price of each by $1
        book.mark_down(1.0);
        cd.mark_down(1.0);

        cout << endl << "Their details in [title, price, rating, total pages/minutes]:" << endl;
        cout << book.to_string() << endl;
        cout << cd.to_string() << endl << endl;

        //  Create array of ItemRecord pointers and call each to_string iteratively
        //array<ItemRecord&, 2> inventory =    ;
        //(const book&)
        //, const cd&};
        //inventory[0] = book;
        //inventory[1] = cd;
        //ItemRecord inventory[2];    
        //inventory[0] = book;
        
    } catch (range_error) {
        cout << "Record entry failed: ";
    }

    //cout << "And if my grandmother had wheels, she'd be a wagon." << endl;
}

