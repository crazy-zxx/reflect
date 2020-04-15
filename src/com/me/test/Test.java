package com.me.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.*;

public class Test {

    public static void main(String[] args) {

        //通过Class实例获取class信息的方法称为反射（Reflection）
        //①
        Class cls1 = String.class;

        //②
        String s = "hello";
        Class cls2 = s.getClass();

        System.out.println(cls1 == cls2);

        try {
            //③
            Class cls3 = Class.forName("java.lang.String");
            System.out.println(cls1 == cls3);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //获取一个class对应的Class实例后，就可以获取该class的所有信息
        printClassInfo("".getClass());
        printClassInfo(Runnable.class);
        printClassInfo(java.time.Month.class);
        printClassInfo(String[].class);
        printClassInfo(int.class);

        /*
        Field getField(name)：根据字段名获取某个public的field（包括父类）
        Field getDeclaredField(name)：根据字段名获取当前类的某个field（不 包括父类）
        Field[] getFields()：获取所有public的field（包括父类）
        Field[] getDeclaredFields()：获取当前类的所有field（不 包括父类）

        一个Field对象包含了一个字段的所有信息：
        getName()：返回字段名称，例如，"name"；
        getType()：返回字段类型，也是一个Class实例，例如，String.class；
        getModifiers()：返回字段的修饰符，它是一个int，不同的bit表示不同的含义
         */
        Class stdClass = Student.class;
        try {
            // 获取public字段"score":
            System.out.println(stdClass.getField("score"));
            // 获取继承的public字段"name":
            System.out.println(stdClass.getField("name"));
            // 获取private字段"grade":
            System.out.println(stdClass.getDeclaredField("grade"));

            //用Field.get(Object)获取指定实例的指定字段的值。
            Object p = new Person("Xiao Ming");
            Class c = p.getClass();
            Field f = c.getDeclaredField("name");
            Object value = f.get(p);
            System.out.println(value); // "Xiao Ming"

            Student student = new Student("xxx", 23, 5);
            Field fs = student.getClass().getDeclaredField("grade");
            //调用Field.setAccessible(true)的意思是，别管这个字段是不是public，一律允许访问。
            fs.setAccessible(true);
            Object val = fs.get(student);
            System.out.println(val);

            //设置字段值是通过Field.set(Object, Object)实现的，
            // 其中第一个Object参数是指定的实例，第二个Object参数是待修改的值。
            Person p1 = new Person("Xiao Ming");
            System.out.println(p1.getName()); // "Xiao Ming"
            Class c1 = p1.getClass();
            Field f1 = c1.getDeclaredField("name");
            f1.setAccessible(true);
            f1.set(p1, "Xiao Hong");
            System.out.println(p1.getName()); // "Xiao Hong"

            //Class类提供了以下几个方法来获取Method：
            //Method getMethod(name, Class...)：获取某个public的Method（包括父类）
            //Method getDeclaredMethod(name, Class...)：获取当前类的某个Method（不包括父类）
            //Method[] getMethods()：获取所有public的Method（包括父类）
            //Method[] getDeclaredMethods()：获取当前类的所有Method（不包括父类）
            Class stdCla = Student.class;
            // 获取public方法getScore，参数为String:
            System.out.println(stdCla.getMethod("hello", String.class));
            // 获取继承的public方法getName，无参数:
            System.out.println(stdCla.getMethod("getName"));
            // 获取private方法getGrade，无参数:
            System.out.println(stdCla.getDeclaredMethod("getGrade"));

            //对Method实例调用invoke就相当于调用该方法，
            // invoke的第一个参数是对象实例，即在哪个实例上调用该方法，
            // 后面的可变参数要与方法参数一致，否则将报错。
            String ss = "Hello world";
            // 获取String substring(int)方法，参数为int:
            Method m = String.class.getMethod("substring", int.class);
            // 在s对象上调用该方法并获取结果:
            String r = (String) m.invoke(ss, 6);
            // 打印调用结果:
            System.out.println(r);

            //如果获取到的Method表示一个静态方法，调用静态方法时，由于无需指定实例对象，
            // 所以invoke方法传入的第一个参数永远为null
            Method mm = Integer.class.getMethod("parseInt", String.class);
            // 调用该静态方法并获取结果:
            Integer n = (Integer) mm.invoke(null, "12345");
            // 打印调用结果:
            System.out.println(n);

            //为了调用任意的构造方法，Java的反射API提供了Constructor对象，
            // 它包含一个构造方法的所有信息，可以创建一个实例。
            // Constructor对象和Method非常类似，不同之处仅在于它是一个构造方法，并且，调用结果总是返回实例
            // 获取构造方法Integer(int):
            Constructor cons1 = Integer.class.getConstructor(int.class);
            // 调用构造方法:
            Integer n1 = (Integer) cons1.newInstance(123);
            System.out.println(n1);

            // 获取构造方法Integer(String)
            Constructor cons2 = Integer.class.getConstructor(String.class);
            Integer n2 = (Integer) cons2.newInstance("456");
            System.out.println(n2);

            //获取父类的Class
            Class i = Integer.class;
            Class nn = i.getSuperclass();
            System.out.println(nn);
            Class o = nn.getSuperclass();
            System.out.println(o);
            System.out.println(o.getSuperclass());

            //getInterfaces()只返回当前类直接实现的接口类型，并不包括其父类实现的接口类型
            Class sss = Integer.class;
            Class[] is = sss.getInterfaces();
            for (Class ii : is) {
                System.out.println(ii);
            }

            //如果是两个Class实例，要判断一个 向上转型 是否成立，可以调用isAssignableFrom():由后转向前
            // true，因为Integer可以赋值给Integer
            System.out.println(Integer.class.isAssignableFrom(Integer.class));
            // true，因为Integer可以赋值给Number
            System.out.println(Number.class.isAssignableFrom(Integer.class));
            // true，因为Integer可以赋值给Object
            System.out.println(Object.class.isAssignableFrom(Integer.class));
            // false，因为Number不能赋值给Integer
            System.out.println(Integer.class.isAssignableFrom(Number.class));


            // 没有实现类但是在运行期动态创建了一个接口对象的方式，我们称为动态代码。
            //JDK提供的动态创建接口对象的方式，就叫动态代理。
            //定义一个InvocationHandler实例，它负责实现接口的方法调用；
            InvocationHandler handler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println(method);
                    if (method.getName().equals("morning")) {
                        System.out.println("Good morning, " + args[0]);
                    }
                    return null;
                }
            };

            //通过JDK提供的一个Proxy.newProxyInstance()创建了一个Hello接口对象。
            Hello hello = (Hello) Proxy.newProxyInstance(
                    Hello.class.getClassLoader(), // 传入ClassLoader
                    new Class[]{Hello.class}, // 传入要实现的接口
                    handler); // 传入处理调用方法的InvocationHandler
            hello.morning("Bob");

            annotationTest();

            //判断某个注解是否存在于Class、Field、Method或Constructor：
            //Class.isAnnotationPresent(Class)
            //Field.isAnnotationPresent(Class)
            //Method.isAnnotationPresent(Class)
            //Constructor.isAnnotationPresent(Class)
            // 判断@Report是否存在于Person类:
            System.out.println(Person.class.isAnnotationPresent(Report.class));
            // 判断@Report是否存在于annotationTest方法:
            System.out.println(
                    Test.class.getDeclaredMethod("annotationTest")
                            .isAnnotationPresent(Report.class));

            //使用反射API读取Annotation：
            //Class.getAnnotation(Class)
            //Field.getAnnotation(Class)
            //Method.getAnnotation(Class)
            //Constructor.getAnnotation(Class)
            //但要读取方法参数的Annotation就比较麻烦一点，因为方法参数本身可以看成一个数组，
            // 而每个参数又可以定义多个注解，所以，一次获取方法参数的所有注解就必须用一个二维数组来表示
            System.out.println(Test.class.getDeclaredMethod("annotationTest")
                    .getAnnotation(Report.class).value());

            //通过@Range注解，配合check()方法，就可以完成People实例的检查
            People people =new People("ttt","beijing");
            //People people =new People("tt555555555555555555555t","beijing");
            people.check();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Report(type = 100, level = "haha", value = "100")
    static void annotationTest() {
        System.out.println("...");
    }

    static void printClassInfo(Class cls) {
        System.out.println("Class name: " + cls.getName());
        System.out.println("Simple name: " + cls.getSimpleName());
        if (cls.getPackage() != null) {
            System.out.println("Package name: " + cls.getPackage().getName());
        }
        System.out.println("is interface: " + cls.isInterface());
        System.out.println("is enum: " + cls.isEnum());
        System.out.println("is array: " + cls.isArray());
        System.out.println("is primitive: " + cls.isPrimitive());
    }
}

//使用 @interface 语法来定义注解（Annotation）
//注解的参数类似无参方法，可以用default设定一个默认值（强烈推荐）。最常用的参数应当命名为value。
//① 使用@Target可以定义Annotation能够被应用于源码的哪些位置：
//类或接口：ElementType.TYPE；
//字段：ElementType.FIELD；
//方法：ElementType.METHOD；
//构造方法：ElementType.CONSTRUCTOR；
//方法参数：ElementType.PARAMETER。
//② 元注解@Retention定义了Annotation的生命周期：
//仅编译期：RetentionPolicy.SOURCE；
//仅class文件：RetentionPolicy.CLASS；(默认)
//运行期：RetentionPolicy.RUNTIME
//③ 使用@Inherited定义子类是否可继承父类定义的Annotation。
// @Inherited仅针对@Target(ElementType.TYPE)类型的annotation有效，
// 并且仅针对class的继承，对interface的继承无效
//必须设置 @Target和@Retention，@Retention一般设置为RUNTIME，因为我们自定义的注解通常要求在运行期读取。
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@interface Report {
    int type() default 0;

    String level() default "info";

    String value() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Range {
    int min() default 0;

    int max() default 255;
}

class People {
    @Range(min = 1, max = 20)
    public String name;

    @Range(max = 10)
    public String city;

    void check() throws IllegalArgumentException, ReflectiveOperationException {
        // 遍历所有Field:
        for (Field field : this.getClass().getFields()) {
            // 获取Field定义的@Range:
            Range range = field.getAnnotation(Range.class);
            // 如果@Range存在:
            if (range != null) {
                // 获取Field的值:
                Object value = field.get(this);
                // 如果值是String:
                if (value instanceof String) {
                    String s = (String) value;
                    // 判断值是否满足@Range的min/max:
                    if (s.length() < range.min() || s.length() > range.max()) {
                        throw new IllegalArgumentException("Invalid field: " + field.getName());
                    }
                }
            }
        }
    }

    public People(String name, String city) {
        this.name = name;
        this.city = city;
    }
}


interface Hello {
    void morning(String name);
}

class Student extends Person {

    public int score;
    private int grade;

    public String hello(String str) {
        System.out.println("hello");
        return str;
    }


    public Student(String name) {
        super(name);
    }

    public Student(String name, int score, int grade) {
        super(name);
        this.score = score;
        this.grade = grade;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}


class Person {

    public String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}