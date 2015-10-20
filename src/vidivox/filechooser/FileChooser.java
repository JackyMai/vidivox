package vidivox.filechooser;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {
	protected static JFileChooser fileChooser = new JFileChooser();
	protected static FileNameExtensionFilter videoFilter = new FileNameExtensionFilter("Video Files (mp4, avi)", "mp4", "avi");
	protected static FileNameExtensionFilter audioFilter = new FileNameExtensionFilter("MP3 Files", "mp3");
}
