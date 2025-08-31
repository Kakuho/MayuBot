package mayubot.cst;

public class CstPrinter{
  public static void Print(ConcreteNode root, int level){
    String outputString = "";
    for(int i = 0; i < level; i++){
      outputString += "\t";
    }
    System.out.println(outputString + root.GetString());
    var childlist = root.GetChildren();
    if(!childlist.isEmpty()){
      for(ConcreteNode child: childlist){
        Print(child, level + 1);
      }
    }
  }

  public static void PrintRoot(ConcreteNode root){
    Print(root, 0);
  }
}
