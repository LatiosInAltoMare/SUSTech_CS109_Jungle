package model;


import java.io.Serializable;

public class ChessPiece implements Serializable {
    // the owner of the chess
    private PlayerColor owner;

    // Elephant? Cat? Dog? ...
    private String name;
    private int rank;

    public ChessPiece(PlayerColor owner, String name, int rank) {
        this.owner = owner;
        this.name = name;
        this.rank = rank;
    }


    public boolean canCapture(ChessPiece target) //如果这可以杀目标则返回true
    {
        //rank从1到7 1<2<3<...<7 7<1
        // TODO: Finish this method!
        if(this.owner.equals(target.owner))
        {
               return false;
        }
        else //不同类
        {
            if(rank==8&&target.getRank()==1)
            {
                return false;
            }
            else if(rank!=1 &&rank>=target.getRank())
            {
                return true;
            }
            else if (rank==1&&target.getRank()==8)
            {
                return true;
            }
            else if(target.getRank()==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    public String getName() {
        return name;
    }
    public int getRank()
    {
        return rank;
    }
    public PlayerColor getOwner() {
        return owner;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
    public ChessPiece clone()
    {
        return new ChessPiece(owner,name,rank);
    }
}
