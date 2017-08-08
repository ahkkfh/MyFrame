package longimage.matisse;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-08-08 13:36
 *
 */
public class CaptureStrategy {
    public final boolean isPublic;
    public final String authority;

    public CaptureStrategy(boolean isPublic, String authority) {
        this.isPublic = isPublic;
        this.authority = authority;
    }
}
