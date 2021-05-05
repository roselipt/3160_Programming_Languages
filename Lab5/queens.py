# 8-queens Problem

def make_board() :
    """Function creates an open chess board as a dictionary and returns it.

    An empty dictionary is populated with tuples, each one a set of coordinates (x, y),
    numbering from top left (0,0), and associated with a value which is set to 0.
    Changing this number to a 1 will represent the placement of a queen.
    """

    d={}
    for col in range(0,8) :
        for row in range(0, 8) :
            d[col, row] = 0
    return d

def show_board(d) :
    for row in range(0,8) :
        print(d[(0,row)],d[(1,row)],d[(2,row)],d[(3,row)],d[(4,row)],d[(5,row)],d[(6,row)],d[(7,row)] )

def test_placement(a, p) :
    #  Borrowed straight from Prof. Raphan's slides
    dif= tuple (x-y for x, y in zip (a[0], p[1]))
    print(dif)
    y, x = dif #The column and row components of the diff tuple are extracted
    print (x)
    print (y)
    #This checks if placement of queen is safe
    if abs(x)==abs(y) or x==0:
        print ("the placement is unsafe")
    else:
        print ("safe placement")
    dif= tuple (x-y for x, y in zip (a[0], p[0]))
    print(dif)
    y, x = dif #The column and row components of the diff tuple are extracted
    print (x)
    print (y)
    #This checks if placement of queen is safe
    if abs(x)==abs(y) or x==0:
        print ("the placement is unsafe")
    else:
        print ("safe placement")
    
    return True

def safe_placement(a, p) :
#  Borrowed straight from Prof. Raphan's slides
    dif= tuple (x-y for x, y in zip (a, p))
    #print(dif)
    y, x = dif #The column and row components of the diff tuple are extracted
    #print (x)
    #print (y)
    #This checks if placement of queen is safe
    if abs(x)==abs(y) or x==0:
        #print ("the placement is unsafe")
        return False
    else:
        #print ("safe placement")
        return True

def placin_agent() :
    print("Hey o")

#def main() :
board = make_board()
show_board(board)
board[(0,0)] = 1
a = [(0,0)]   #  a will hold list of placed queens
print("\nFirst placement is set above but it may be changed")
print("to any position in column 1 to generate a different solution.")
show_board(board)
print()
didnt_work = []
back_track = []
    #  We can assume one queen per column as part of problem design
col = 1
row = 0
exit = False
while not exit :
    print(len(a))
    while len(a) != 8 :
        safe = True  #  will hold results of tests for all placed queens
        #  Test this potential placement against all previously placed queens
        for q in a :
            if not safe_placement(q, (col,row)) :
                safe = False
        #  if safe against all queens already placed
        if safe :
            #  place queen, show board, increment search position to next column
            a.append((col,row))  
            board[(col,row)] = 1  
            show_board(board)
            print("  Queen is placed! ", (col,row), "\n")
            col = col + 1
            row = 0
        #  if search reaches last row in column without finding safe placement
        elif row == 7 :
            print ("Drat! no safe position in col ", col, " must backtrack")
            didnt_work = a[:]  # save didnt_work up to impossible row
            #  HOW DO WE KNOW IF WE'VE BACKTRACKED YET
            #print ("Must devise test against previous backtracking.")
            #  If no backtracking has occurred yet
            if back_track == [] :
                #print("This behavior is for first backtrack only. (Or it happens once.)")
                back_track.append(col-1)  # record column that failed in back_track list
                print (" Didnt_work is ", didnt_work)
                print (" Back_track is ", back_track)
                x, y = a.pop()    # remove last successful queen placement (which was in col -1 )
                                  # and get its position in x, y
                board[(x,y)] = 0  # update board
                # IF spaces remain to test in previous column
                if y < 7 :
                    print("Trying remaining rows in last column.")
                    col = x
                    row = y + 1   # begin testing again at next row from now rejected safe queen placement
                # previous column has been completely tested
                print("Must devise logic for backtracking when last column is row 7 first try.")
                print("  ... or do I?")
            #  Else case where backtracking must have occurred already
            else :    
                print(" This behavior is used for every backtrack after first.")
                print(" It looks at column of last saved back_track, and backtracks to the one before.")
                print("  This time back_track is column", back_track[-1])
                x, y = a.pop()    # remove last successful queen placement
                board[(x,y)] = 0  # update board
                #  Make certain to backtrack to before last back_track
                #  or before last column if we had completely tested it
                while x >= back_track[-1] or y == 7 :
                        x, y = a.pop()    # remove one more successful queen placement (which was in row - )
                        board[(x,y)] = 0  # update board
                # IF spaces remain to test in column
                # if y < 7 : (DONT NEED THIS IT MUST NOT BE 7)
                col = x
                row = y + 1   # begin testing again at next row from rejected safe queen placement
                print("  So checking begins again at ", col, row)
                print("\n")
        
        #  increment row after safe test has failed and not yet row == 7
        else : row = row + 1
    exit = True

        # if back_track instruction exists
        # (which means made it to row 7 with no good placement)
#        if back_track :
#            col = back_track.pop()  
#            x,y = didnt_work.pop()
#            row =  y+1
#        else :
#            row = row + 1
#    col = col + 1
print("One solution is ", a)    
print("\nahem.")

#main()

#    a = [k for k,v in board.items() if v == 1]
#    print("List 'a' of queens is", a)
