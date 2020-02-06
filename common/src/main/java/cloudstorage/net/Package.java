package cloudstorage.net;

public abstract class Package {

    protected boolean isHeadSent = false;
    protected boolean isBodySent = false;

    abstract byte[] getBodyBytes();

    abstract byte[] getHeadBytes();

    abstract byte getFlag();

    public byte[] getBytes() {

        if (!this.isHeadSent) {
            this.isHeadSent = true;
            return this.getHead();
        }

        if (!this.isBodySent) {
            return this.getBody();
        }

        return new byte[0];
    }

    private byte[] getHead() {

        byte[] headBytes = this.getHeadBytes();
        byte[] head = new byte[headBytes.length + 1];
        head[0] = this.getFlag();
        System.arraycopy(headBytes, 0, head, 1, headBytes.length);

        return head;
    }

    private byte[] getBody() {

        byte[] body = this.getBodyBytes();
        if (body.length == 0) {
            this.isBodySent = true;
        }

        return body;
    }
}
