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

	private ArrayList<File> hitFileList;

	private ArrayList<Map> FileMapList;

	public SearchFile(){
	}

	public SearchFile(String rootPath){
		this.rootPath = rootPath;
	}

	public ArrayList<Map> getFileMapList() {
		return this.FileMapList;
	}

	// ファイルを再帰処理で検索
	public ArrayList<File> recursionSearchFileName(ArrayList<File> hitFileList, String targetFileName) throws IOException{
		System.out.println("ファイル名検索");
		File searchDir = new File(this.rootPath);

		File objlist[] = searchDir.listFiles();

		for (File obj : objlist){
			if (obj.isDirectory()){
				//取得したオブジェクトがディレクトリの場合、再帰処理を行う
				this.rootPath = obj.getCanonicalPath();
				hitFileList = recursionSearchFileName(hitFileList, targetFileName);
			}else if (obj.isFile()){
				System.out.println(targetFileName);
				System.out.println(obj.getName());
				//取得したオブジェクトがファイルの場合、ファイル名が一致するかを確認する。
				if (targetFileName.equals(obj.getName())){
					hitFileList.add(obj);
				}
			}
		}
		return hitFileList;
	}

	// ファイルを再帰処理で検索
	public ArrayList<File> recursionSearchTargetDate(ArrayList<File> hitFileList, String targetDate) throws IOException{
		System.out.println("更新日付検索");

		File searchDir = new File(this.rootPath);

		File objlist[] = searchDir.listFiles();

		for (File obj : objlist){
			if (obj.isDirectory()){
				//取得したオブジェクトがディレクトリの場合、再帰処理を行う
				if (obj.getCanonicalPath().indexOf(targetDate) > -1){
					this.rootPath = obj.getCanonicalPath();
					hitFileList = recursionSearchTargetDate(hitFileList, targetDate);
				}
			}else if (obj.isFile()){
				//取得したオブジェクトがファイルの場合、ファイル名が一致するかを確認する。
				if (obj.getCanonicalPath().indexOf(targetDate) > -1){
					hitFileList.add(obj);
				}
			}
		}
		return hitFileList;
	}

	// ヒットしたファイルをMap化する
	public void convertFileMapping(ArrayList<File> hitFileList) throws IOException {
		for (File file : hitFileList) {
			//リストへ格納するための連想配列を用意
			Map<String, Object> fileMap = new HashMap<>();

			//ファイルの名前
			String filename = file.getName();

			//ファイルの更新日時
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Long lastModified = file.lastModified();
			String filedate = sdf.format(lastModified);

			//ファイルの格納されている絶対パスを取得
			String filePath;
			filePath = file.getAbsolutePath();

			//ファイルテキストを１行ずつリスト化
			ArrayList<String> filetext = null;
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = br.readLine();
			while(str != null){
				filetext.add(str);
				str = br.readLine();
			}
			br.close();

			fileMap.put("file_name", filename);
			fileMap.put("file_date", filedate);
			fileMap.put("file_path", filePath);
			fileMap.put("file_text", filetext);

			this.FileMapList.add(fileMap);
		}
	}

}
