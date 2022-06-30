package reptile;

public class LiLI {

    public static void main(String[] args) {
        String str = "<span class=\"op-stockdynamic-cur-info c-gap-right-small\"> (0.27%) </span>";

        if (str.contains("<span class=\"op-stockdynamic-cur-info")) {
            String[] split = str.split("<span class=\"op-stockdynamic-cur-info");
            String[] s = split[1].split(">");
            int begain = split[1].indexOf(">");
            String substring = split[1].substring(split[1].indexOf(">") + 2, 29);
            String trim = substring.replace("(", "").replace(")", "").trim();
            System.out.println(trim);
        }
    }

}
