package data;
import static core.dataStructure.MarkedPath.markOneStatement;
public class CloneFile {
public static void function(boolean A, boolean B, boolean C, boolean X, boolean Y)
{
if (((A || X && Y) && markOneStatement("A || X && Y", true, false)) || markOneStatement("A || X && Y", false, true))
{
markOneStatement("System.out.println(A);\n", false, false);
System.out.println(A);
}
if (((B) && markOneStatement("B", true, false)) || markOneStatement("B", false, true))
{
markOneStatement("System.out.println(B);\n", false, false);
System.out.println(B);
}
else if (((C) && markOneStatement("C", true, false)) || markOneStatement("C", false, true))
{
markOneStatement("System.out.println(C);\n", false, false);
System.out.println(C);
}
}
}
