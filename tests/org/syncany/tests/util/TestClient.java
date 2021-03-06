/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2013 Philipp C. Heckel <philipp.heckel@gmail.com> 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.tests.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.syncany.Client;
import org.syncany.config.Config;
import org.syncany.connection.plugins.Connection;
import org.syncany.database.Database;
import org.syncany.database.DatabaseDAO;
import org.syncany.database.XmlDatabaseDAO;
import org.syncany.operations.StatusOperation.StatusOperationOptions;
import org.syncany.operations.UpOperation.UpOperationOptions;
import org.syncany.operations.UpOperation.UpOperationResult;
import org.syncany.operations.WatchOperation.WatchOperationOptions;

public class TestClient extends Client {
	public TestClient(String machineName, Connection connection) throws Exception {
		Config testConfig = TestConfigUtil.createTestLocalConfig(machineName, connection);
		testConfig.setMachineName(machineName);
		
		this.setConfig(testConfig);
	}	
	
	public UpOperationResult upWithForceChecksum() throws Exception {
		StatusOperationOptions statusOptions = new StatusOperationOptions();
		statusOptions.setForceChecksum(true);
		
		UpOperationOptions upOptions = new UpOperationOptions();
		upOptions.setStatusOptions(statusOptions);
		
		return up(upOptions);
	}
	
	public Thread watchAsThread(final int interval) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					WatchOperationOptions watchOperationOptions = new WatchOperationOptions();
					watchOperationOptions.setInterval(interval);
					
					watch(watchOperationOptions);
				}
				catch (Exception e) {
					throw new RuntimeException(e);
				}
			}			
		});
	}
	
	public void createNewFiles() throws IOException {
		TestFileUtil.createRandomFilesInDirectory(config.getLocalDir(), 25*1024, 20);		
	}
	
	public void createNewFiles(String inFolder) throws IOException {
		TestFileUtil.createRandomFilesInDirectory(getLocalFile(inFolder), 25*1024, 20);		
	}
	
	public File createNewFile(String name) throws IOException {
		return createNewFile(name, 50*1024);
	}
	
	public File createNewFile(String name, long size) throws IOException {
		File localFile = getLocalFile(name);		
		TestFileUtil.createRandomFile(localFile, size);
		
		return localFile;
	}
	
	public void createNewFolder(String name) {
		getLocalFile(name).mkdirs();		
	}	
	
	public void moveFile(String fileFrom, String fileTo) throws Exception {
		File fromLocalFile = getLocalFile(fileFrom);
		File toLocalFile = getLocalFile(fileTo);
		
		try {
			if (fromLocalFile.isDirectory()) {
				FileUtils.moveDirectory(fromLocalFile, toLocalFile);
			}
			else {
				FileUtils.moveFile(fromLocalFile, toLocalFile);
			}
		}
		catch (Exception e) {		
			throw new Exception("Move failed: "+fileFrom+" --> "+fileTo, e);
		}		
	}	
	
	public void copyFile(String fileFrom, String fileTo) throws IOException {
		FileUtils.copyFile(getLocalFile(fileFrom), getLocalFile(fileTo));		
	}	

	public void changeFile(String name) throws IOException {
		TestFileUtil.changeRandomPartOfBinaryFile(getLocalFile(name));		
	}	
	
	public boolean deleteFile(String name) {
		return FileUtils.deleteQuietly(getLocalFile(name));		
	}	
	
	public void cleanup() {
		TestConfigUtil.deleteTestLocalConfigAndData(config);
	}
	
	public File getLocalFile(String name) {
		return new File(config.getLocalDir()+"/"+name);
	}
	
	public Map<String, File> getLocalFiles() throws FileNotFoundException {
		return TestFileUtil.getLocalFiles(config.getLocalDir());		
	}
 
	public File getLocalDatabaseFile() {
		return config.getDatabaseFile();
	}

	public File getDirtyDatabaseFile() {
		return config.getDirtyDatabaseFile();
	}
	
	public Database loadLocalDatabase() throws IOException {
		File localDatabaseFile = getLocalDatabaseFile();
		Database db = new Database();
		
		if (localDatabaseFile.exists()) {			
			DatabaseDAO dao = new XmlDatabaseDAO();
			dao.load(db, getLocalDatabaseFile());		
		}

		return db;
	}
}
