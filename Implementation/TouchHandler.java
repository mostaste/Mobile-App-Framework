package Implementation;

import android.view.View;

import com.JomoFramework.Input.TouchEvent;

import java.util.List;

public interface TouchHandler extends View.OnTouchListener
{
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();
}
