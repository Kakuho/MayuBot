package mayubot.cst;

import mayubot.lex.TokenType;
import mayubot.ast.AbstractNode;
import mayubot.ast.OpNode;
import mayubot.ast.Operator;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.lang.RuntimeException;

public interface ConcreteNode{
  public String GetString();
  public List<ConcreteNode> GetChildren();
  public AbstractNode GetAst();
}

class IntegralNode implements ConcreteNode{
  public long val;

  public IntegralNode(long val){
    this.val = val;
  }

  @Override
  public String GetString(){
    return String.format("Integral: %d", val);
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    return new ArrayList<ConcreteNode>();
  }

  @Override
  public AbstractNode GetAst(){
    return new mayubot.ast.IntegerNode(val);
  }
}

class FloatingNode implements ConcreteNode{
  public double val;

  public FloatingNode(double val){
    this.val = val;
  }

  @Override
  public String GetString(){
    return String.format("Floating: %d", val);
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    return new ArrayList<ConcreteNode>();
  }

  @Override
  public AbstractNode GetAst(){
    return new mayubot.ast.FloatingNode(val);
  }
}

class BNode implements ConcreteNode{
  public enum ChildType{Integral, Floating, ENode};
  private ChildType childtype;
  private ConcreteNode child;

  public BNode(IntegralNode node){
    this.child = node;
    this.childtype = ChildType.Integral;
  }

  public BNode(FloatingNode node){
    this.child = node;
    this.childtype = ChildType.Floating;
  }

  @Override
  public String GetString(){
    return String.format("B node");
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    return List.of(child);  // how does type resolution work here in java?
  }

  @Override
  public AbstractNode GetAst(){
    if(childtype == ChildType.ENode){
      return null;
    }
    else{
      return child.GetAst();
    }
  }
}

class FNode implements ConcreteNode{
  public enum ChildType{Power, Bnode};
  private ChildType childtype;
  private ConcreteNode first;
  private Optional<ConcreteNode> second;

  public FNode(BNode lower, BNode upper){
    this.first = lower;
    this.second = Optional.of(upper);
    this.childtype = ChildType.Power;
  }

  public FNode(BNode node){
    this.first = node;
    this.childtype = ChildType.Bnode;

    this.second = Optional.empty();
  }

  @Override
  public String GetString(){
    if(childtype == ChildType.Power){
      return "F node: ^";
    }
    else{
      return String.format("F node");
    }
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    if(childtype == ChildType.Power){
      return List.of(first, second.get());
    }
    else{
      return List.of(first);  // how does type resolution work here in java?
    }
  }

  @Override
  public AbstractNode GetAst(){
    if(childtype == ChildType.Power){
      return new OpNode(Operator.Pow, first.GetAst(), second.get().GetAst());
    }
    else{
      return first.GetAst();
    }
  }
}

class TNode implements ConcreteNode{
  public enum ChildType{Op, Fnode};

  private ChildType childtype;
  private Optional<TokenType> op;
  private ConcreteNode first;
  private Optional<ConcreteNode> second;

  public TNode(TokenType operator, FNode lhs, TNode rhs){
    this.first = lhs;
    this.second = Optional.of(rhs);
    this.op = Optional.of(operator);
    this.childtype = ChildType.Op;
  }

  public TNode(FNode node){
    this.first = node;
    this.childtype = ChildType.Fnode;

    this.second = Optional.empty();
    this.op = Optional.empty();
  }

  @Override
  public String GetString(){
    if(op.isEmpty()){
      return "T node";
    }
    else if(op.get() == TokenType.Divide){
      return "T node: /";
    }
    else if(op.get() == TokenType.Multiply){
      return "T node: *";
    }
    else{
      throw new RuntimeException("Tnode.GetString(): Unknown Operator");
    }
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    if(childtype == ChildType.Op){
      return List.of(first, second.get());
    }
    else{
      return List.of(first);  // how does type resolution work here in java?
    }
  }

  @Override
  public AbstractNode GetAst(){
    if(childtype == ChildType.Fnode){
      return first.GetAst();
    }
    else if(op.get() == TokenType.Divide){
      return new OpNode(Operator.Div, first.GetAst(), second.get().GetAst());
    }
    else{
      return new OpNode(Operator.Multiply, first.GetAst(), second.get().GetAst());
    }
  }
}


class ENode implements ConcreteNode{
  public enum ChildType{Op, Tnode};

  private ChildType childtype;
  private Optional<TokenType> op;
  private ConcreteNode first;
  private Optional<ConcreteNode> second;

  public ENode(TokenType operator, TNode lhs, ENode rhs){
    this.first = lhs;
    this.second = Optional.of(rhs);
    this.op = Optional.of(operator);
    this.childtype = ChildType.Op;
  }

  public ENode(TNode node){
    this.first = node;
    this.childtype = ChildType.Tnode;

    this.second = Optional.empty();
    this.op = Optional.empty();
  }

  @Override
  public String GetString(){
    if(op.get() == TokenType.Plus){
      return "E node: +";
    }
    else if(op.get() == TokenType.Minus){
      return "E node: -";
    }
    else{
      return "E node";
    }
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    if(childtype == ChildType.Op){
      return List.of(first, second.get());
    }
    else{
      return List.of(first);  // how does type resolution work here in java?
    }
  }

  @Override
  public AbstractNode GetAst(){
    if(childtype == ChildType.Tnode){
      return first.GetAst();
    }
    else if(op.get() == TokenType.Plus){
      return new OpNode(Operator.Plus, first.GetAst(), second.get().GetAst());
    }
    else{
      return new OpNode(Operator.Sub, first.GetAst(), second.get().GetAst());
    }
  }
}

class FunctionNameNode implements ConcreteNode{
  private final String name;

  public FunctionNameNode(String name){
    this.name = name;
  }

  public String GetName(){
    return this.name;
  }

  @Override
  public String GetString(){
    return String.format("FunctionName: %s", name);
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    return List.of();
  }

  @Override
  public AbstractNode GetAst(){
    // probably should remove this...
    // maybe able to skip the parsing of the FunctionNameNode
    return null;
  }
}

class FunctionCallNode implements ConcreteNode{
  private final FunctionNameNode nameNode;
  private final ConcreteNode expr;

  public FunctionCallNode(FunctionNameNode nameNode, ENode expr){
    this.nameNode = nameNode;
    this.expr = expr;
  }

  @Override
  public String GetString(){
    return "Function";
  }

  @Override
  public List<ConcreteNode> GetChildren(){
    return List.of(nameNode, expr);
  }

  @Override
  public AbstractNode GetAst(){
    return new mayubot.ast.FunctionCallNode(nameNode.GetName(), expr.GetAst());
  }
}
