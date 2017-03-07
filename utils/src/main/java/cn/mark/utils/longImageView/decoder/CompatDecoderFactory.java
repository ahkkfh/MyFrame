package cn.mark.utils.longImageView.decoder;

/***
 * @author marks.luo
 * @Description: TODO()
 * @date:2017-03-07 17:34
 */

public class CompatDecoderFactory<T> implements DecoderFactory<T> {
    private Class<? extends T> clazz;

    public CompatDecoderFactory(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T make() throws IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }
}
