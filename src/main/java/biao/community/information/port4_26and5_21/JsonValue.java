package biao.community.information.port4_26and5_21;

/**
 * @author WuJunHui
 * @date 2023/4/26 20:27
 */
public class JsonValue {


        String u_id;
        String time;
        int sum;
    boolean b_anonymous;

    public boolean isB_anonymous() {
        return b_anonymous;
    }

    public void setB_anonymous(boolean b_anonymous) {
        this.b_anonymous = b_anonymous;
    }

    public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getSum() {
            return sum;
        }

        public void setSum(int sum) {
            this.sum = sum;
        }


}
