package ro.Personal.email;

public class CleanupTask implements Runnable {

	@Override
	public void run() {
		// entry point for Thread.start

		while (true) {
			try {
				Thread.sleep(10 * 1000);

				MainEntry.cleanupOldAccounts();

			} catch (Exception e) {
				System.err.println("There was an error cleaning up the accounts");
			}

		}
	}
}
