package vidivox;
import java.awt.EventQueue;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

/* 
 * Main: This class acts as an entry point and initialises the program 
 * 
 * Authors: Helen Zhao, Jacky Mai
 * UPI: hzha587, jmai871
 */
public class Main {
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),
				"/Applications/vlc-2.0.0/VLC.app/Contents/MacOS/lib");
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					VidivoxGUI frame = new VidivoxGUI();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}