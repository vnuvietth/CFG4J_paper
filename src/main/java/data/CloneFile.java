package data;import java.util.ArrayList;
import core.dataStructure.MarkedStatement;
public class CloneFile {
public static int fibonacci(int n)
{
MarkedStatement.markOneStatement("int a=0, b=1, c, i;\n", false, false);
int a=0, b=1, c, i;
if (((n == 0) && MarkedStatement.markOneStatement("n == 0", true, false)) || MarkedStatement.markOneStatement("n == 0", false, true))
{
MarkedStatement.markOneStatement("return a;\n", false, false);
return a;
}
MarkedStatement.markOneStatement("i=2", false, false);
for (i=2; ((i <= n) && MarkedStatement.markOneStatement("i <= n", true, false)) || MarkedStatement.markOneStatement("i <= n", false, true); MarkedStatement.markOneStatement("i++", false, false),
i++) {
{
MarkedStatement.markOneStatement("c=a + b;\n", false, false);
c=a + b;
MarkedStatement.markOneStatement("a=b;\n", false, false);
a=b;
MarkedStatement.markOneStatement("b=c;\n", false, false);
b=c;
}
}
MarkedStatement.markOneStatement("return b;\n", false, false);
return b;
}
}
