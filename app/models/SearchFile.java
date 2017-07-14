package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SearchFile {

	private String rootPath;

	private ArrayList<File> hitFileList = new ArrayList<File>();

	private ArrayList<Map<String, Object>> fileMapList = new ArrayList<Map<String, Object>>();

	public SearchFile(){
	}

	public SearchFile(String rootPath){
		this.rootPath = rootPath;
	}

	public ArrayList<Map<String, Object>> getFileMapList() {
		return this.fileMapList;
	}

	// ファイルを再帰処理で検索
	public void recursionSearchFileName(String targetFileName) throws IOException{
		System.out.println("File Name Search");
		System.out.println("targetFileName : " + targetFileName);
		File searchDir = new File(this.rootPath);

		File objlist[] = searchDir.listFiles();
		System.out.println("this.rootPath : " + this.rootPath);
		System.out.println("objlist[] : " + objlist);
		for (File obj : objlist){
			if (obj.isDirectory()){
				//取得したオブジェクトがディレクトリの場合、再帰処理を行う
				this.rootPath = obj.getCanonicalPath();
				recursionSearchFileName(targetFileName);
			}else if (obj.isFile()){
				//取得したオブジェクトがファイルの場合、ファイル名が一致するかを確認する。
				if (targetFileName.equals(obj.getName())){
					this.hitFileList.add(obj);
				}
			}
		}
	}

	// ファイルを再帰処理で検索
	public void recursionSearchTargetDate(String targetDate) throws IOException{
		System.out.println("File Date Search");

		File searchDir = new File(this.rootPath);

		File objlist[] = searchDir.listFiles();

		for (File obj : objlist){
			if (obj.isDirectory()){
				//取得したオブジェクトがディレクトリの場合、再帰処理を行う
				this.rootPath = obj.getCanonicalPath();
				recursionSearchTargetDate(targetDate);

			}else if (obj.isFile()){
				//取得したオブジェクトがファイルの場合、ファイル名が一致するかを確認する。
				if (targetDate.equals(timeToString(obj, "yyyyMMdd"))){
					this.hitFileList.add(obj);
				}
			}
		}
	}
	public String timeToString(File file, String dateFormat){
		//ファイルの更新日時
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		Long lastModified = file.lastModified();
		String fileDate = sdf.format(lastModified);
		return fileDate;
	}

	// ヒットしたファイルをMap化する
	@SuppressWarnings("null")
	public void convertFileMapping() throws IOException {
		for (File file : this.hitFileList) {
			//リストへ格納するための連想配列を用意
			Map<String, Object> fileMap = new HashMap<>();

			//ファイルの名前
			String filename = file.getName();

			//ファイルの更新日時
			String filedate = timeToString(file, "yyyy年MM月dd日 HH:mm.ss");

			//ファイルの格納されている絶対パスを取得
			String filePath;
			filePath = file.getAbsolutePath();

			//ファイルテキストを１行ずつリスト化
			ArrayList<String> filetext = new ArrayList<String>();
			//filetext = null;
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str;
			while((str = br.readLine()) != null){
				filetext.add(str);
			}
			br.close();

			fileMap.put("file_name", filename);
			fileMap.put("file_date", filedate);
			fileMap.put("file_path", filePath);
			fileMap.put("file_text", filetext);

			this.fileMapList.add(fileMap);
		}
	}

}
