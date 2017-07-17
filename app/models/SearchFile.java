package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * SearchFileクラス
 *
 *  ルートディレクトリより目的のファイルを探すためのクラス。
 *  検索対象としてサブディレクトリも再帰的に探しに行く。
 *
 *  - 検索方法(インスタンス化の際にルートディレクトリのパスを設定しておくことが前提)
 *       検索方法は下記の３つのメソッドとなる。
 *       検索後はFileオブジェクトのArrayList形式でhitFileListへ格納される。
 *     １，ファイル名のみ
 *            recursionSearchFileName(String targetFileName)
 *     ２、更新日時のみ
 *            recursionSearchTargetDate(String targetDate)
 *     ３、ファイル名、更新日時両方
 *            recursionSearchDual(String targetFileName, String targetDate)
 *
 *　- 検索後の利用方法
 *       hitFileListを連想配列へ変換し、getterで取得して利用する。
*/

public class SearchFile {

	private String rootPath;

	private ArrayList<File> hitFileList = new ArrayList<File>();

	private ArrayList<Map<String, Object>> fileMapList = new ArrayList<Map<String, Object>>();

	/**
	 * デフォルトコンストラクタ
	*/
	public SearchFile(){
	}

	/**
	 * デフォルトコンストラクタ（オーバーロード
	 *
	 *  setter
	 *  インスタンス化と同時に指揮数で渡したファイルパスが
	 *  当インスタンスが検索する対象のルートディレクトリとなる。
	*/
	public SearchFile(String rootPath){
		this.rootPath = rootPath;
	}

	/**
	 * getFileMapList()
	 *
	 *  getterクラス。
	 *  当クラスで作成された連想配列ArrayListを
	 *  戻す。
	 *
	 *  引数
	 *      なし
	 *
	 *  戻り値
	 *      ArrayList<Map<String, Object>> this.fileMapList
	 *
	 *    連想配列（String : Object）のArrayList
	 *        {
	 *          { "file_name" : "EP100.txt"
	 *            "file_date" : "2017年12月12日 23:13.28"
	 *            "file_path" : "C:\backup\EP\20171212\EP100.txt"
	 *            "file_text" : "ファイルの中身の文字列"
	 *          },
	 *          {上記と同様の内容
	 *          }
	 *        }
	*/
	public ArrayList<Map<String, Object>> getFileMapList() {
		return this.fileMapList;
	}

	/**
	 * recursionSearchFileName(String targetFileName)
	 *  ファイル名をキーに名前が一致するファイルを検索する。
	 *  一致するすべてのファイルをarrayListへ格納する。
	 *
	 *  引数
	 *      String targetFileName
	 *
	 *  戻り値
	 *      なし
	*/
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

	/**
	 * recursionSearchTargetDate(String targetDate)
	 *  更新日時をキーに日付が一致するファイルを検索する。
	 *  一致するすべてのファイルをarrayListへ格納する。
	 *
	 *  引数
	 *      String targetDate
	 *
	 *  戻り値
	 *      なし
	*/
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
				//取得したオブジェクトがファイルの場合、更新日時が一致するかを確認する。
				if (targetDate.equals(timeToString(obj, "yyyyMMdd"))){
					this.hitFileList.add(obj);
				}
			}
		}
	}

	/**
	 * recursionSearchDual(String targetFileName, String targetDate)
	 *  ファイル名と更新日時をキーに両方のキーと一致するファイルを検索する。
	 *  一致するすべてのファイルをarrayListへ格納する。
	 *
	 *  引数
	 *      String targetFileName
	 *      String targetDate
	 *
	 *  戻り値
	 *      なし
	*/
	public void recursionSearchDual(String targetFileName, String targetDate) throws IOException{
		System.out.println("Dual Search");

		File searchDir = new File(this.rootPath);

		File objlist[] = searchDir.listFiles();

		for (File obj : objlist){
			if (obj.isDirectory()){
				//取得したオブジェクトがディレクトリの場合、再帰処理を行う
				this.rootPath = obj.getCanonicalPath();
				recursionSearchTargetDate(targetDate);

			}else if (obj.isFile()){
				//取得したオブジェクトがファイルの場合、ファイル名が一致するかを確認する。
				if (targetFileName.equals(obj.getName()) && targetDate.equals(timeToString(obj, "yyyyMMdd"))){
					this.hitFileList.add(obj);
				}
			}
		}
	}

	/**
	 * timeToString(File file, String dateFormat)
	 *  FIleオブジェクトの更新日時をdataFormatとして受け取った日付の
	 *  フォーマットへ変換し、変換後の更新日時を返す。
	 *
	 *  引数
	 *      File file
	 *      String dateFormat
	 *
	 *  戻り値
	 *      String fileDate
	*/
	public String timeToString(File file, String dateFormat){
		//ファイルの更新日時
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		Long lastModified = file.lastModified();
		String fileDate = sdf.format(lastModified);
		return fileDate;
	}

	/**
	 * convertFile2Mapping()
	 *
	 *  当クラスで作成されたFileオブジェクトのArrayListを
	 *  連想配列（String : Object）へ変換する。
	 *
	 *    連想配列（String : Object）のArrayList
	 *        {
	 *          { "file_name" : "EP100.txt"
	 *            "file_date" : "2017年12月12日 23:13.28"
	 *            "file_path" : "C:\backup\EP\20171212\EP100.txt"
	 *            "file_text" : "ファイルの中身の文字列"
	 *          },
	 *          {上記と同様の内容
	 *          }
	 *        }
	 *
	 *  引数
	 *      なし
	 *
	 *  戻り値
	 *      なし
	*/
	@SuppressWarnings("null")
	public void convertFile2Mapping() throws IOException {
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
