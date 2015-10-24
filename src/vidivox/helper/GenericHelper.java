package vidivox.helper;

import java.io.File;

public class GenericHelper {
	public static void createDir() {
		File vidivoxDir = new File(".vidivox_jmai871");
		vidivoxDir.mkdirs();
	}
}
