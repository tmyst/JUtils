import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileMerger {
    private static final String DIST_FILE_NAME = "merged";
    private static final String DEFAULT_FILE_EXTENTION = ".csv";
    private static final int DEFAULT_NUM_HEADER = 1;
    private static final int DEFAULT_NUM_SKIPROWS = 0;
    private List<String> srcFilePathList;
    private String distRootPath = FileUtils.getUserDirectoryPath();
    private String distFileName = DIST_FILE_NAME;
    private String fileExtention = DEFAULT_FILE_EXTENTION;
    private int numHeader = DEFAULT_NUM_HEADER;
    private int numSkipRows = DEFAULT_NUM_SKIPROWS;
    private String colNameFilePath = null;

    public static void main(String[] args) {
        FileMerger fm = new FileMerger(new String[]{
                "D:/data/UCI/donation/block_1.csv",
                "D:/data/UCI/donation/block_2.csv",
                "D:/data/UCI/donation/block_3.csv",
                "D:/data/UCI/donation/block_4.csv",
                "D:/data/UCI/donation/block_5.csv",
                "D:/data/UCI/donation/block_6.csv",
                "D:/data/UCI/donation/block_7.csv",
                "D:/data/UCI/donation/block_8.csv",
                "D:/data/UCI/donation/block_9.csv",
                "D:/data/UCI/donation/block_10.csv"
        });
        fm.setDistRoot_("D:/data/out/");
        fm.setNumHeader_(1);
        fm.setNumSkipRows_(0);
        fm.mergeAndWright(false, ",");
    }

    public FileMerger(String[] srcFilePaths){
        this(srcFilePaths, DIST_FILE_NAME);
    }
    public FileMerger(String[] srcFilePaths, String distRootPath){
        this.srcFilePathList=Arrays.stream(srcFilePaths).collect(Collectors.toList());
        this.distRootPath = distRootPath;
    }

    public FileMerger setDistRoot(String distRoot){
        this.distRootPath = distRoot;
        return this;
    }
    public FileMerger setDistFileName(String distFileName){
        this.distFileName = distFileName;
        return this;
    }
    public FileMerger setSrcFilePaths(String[] srcFilePaths){
        this.srcFilePathList = Arrays.stream(srcFilePaths).collect(Collectors.toList());
        return this;
    }
    public FileMerger setColNameFile(String colNameFilePath){
        this.colNameFilePath = colNameFilePath;
        return this;
    }
    public FileMerger setNumHeader(int numHeader){
        this.numHeader=numHeader;
        return this;
    }
    public FileMerger setNumSkipRows(int numSkipRows){
        this.numSkipRows=numSkipRows;
        return this;
    }

    public void setDistRoot_(String distRoot){
        this.distRootPath = distRoot;
    }
    public void setDistFileName_(String distFileName){
        this.distFileName = distFileName;
    }
    public void setSrcFilePaths_(String[] srcFilePaths){
        this.srcFilePathList = Arrays.stream(srcFilePaths).collect(Collectors.toList());
    }
    public void setColNameFile_(String colNameFilePath){
        this.colNameFilePath = colNameFilePath;
    }
    public void setNumHeader_(int numHeader){
        this.numHeader=numHeader;
    }
    public void setNumSkipRows_(int numSkipRows){
        this.numSkipRows=numSkipRows;
    }

    public void mergeAndWright(Boolean headerFromFile, String separator){
        File dist = new File(distRootPath + "/" + distFileName + this.fileExtention);
        int numHeaderToRead = this.numHeader;
        Collections.sort(srcFilePathList);
        try{
            PrintWriter writer = new PrintWriter(dist);
            if(headerFromFile) {
                if (colNameFilePath != null) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(colNameFilePath)));
                    for(int i=0; i<this.numHeader; i++){
                        String headerLine = bf.readLine();
                        writer.println(headerLine);
                    }
                    bf.close();
                    numHeaderToRead = 0;
                }
            } else {
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(srcFilePathList.get(0))));
                for (int i = 0; i < numHeaderToRead; i++) {
                    String headLine = bf.readLine();
                    writer.println(headLine);
                }
            }
            int nTotalSkips=numHeaderToRead+numSkipRows;
            srcFilePathList.forEach(file -> {
                LineIterator it=null;
                    try {
                        it = FileUtils.lineIterator(new File(file));
                        String line;
                        for(int i=0; i<nTotalSkips; i++){
                            it.next();
                        }
                        while(it.hasNext()){
                            line = it.nextLine();
                            writer.println(line);
                        }
                        it.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            );
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void write(){

    }
}
