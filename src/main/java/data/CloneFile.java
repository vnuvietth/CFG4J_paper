package data;
import java.util.ArrayList;
import static core.dataStructure.MarkedPath.markOneStatement;
public class CloneFile {
public static void function(boolean A, boolean B, boolean C, boolean X, boolean Y)
{
if ((((A) && markOneStatement("A", true, false)) || markOneStatement("A", false, true)) || ((((X) && markOneStatement("X", true, false)) || markOneStatement("X", false, true)) && (((Y) && markOneStatement("Y", true, false)) || markOneStatement("Y", false, true))))
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
