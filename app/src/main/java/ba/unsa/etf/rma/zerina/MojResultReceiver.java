package ba.unsa.etf.rma.zerina;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by zerin on 5/19/2018.
 */

public class MojResultReceiver extends ResultReceiver {
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public MojResultReceiver(Handler handler) {
        super(handler);
    }

    private Receiver mReceiver;

    public void setReceiver(Receiver receiver){
        mReceiver = receiver;
    }

    public interface Receiver {
        public  void  onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        //super.onReceiveResult(resultCode, resultData);
        if(mReceiver != null){
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
