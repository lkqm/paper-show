package app

import (
	"archive/zip"
	"io"
	"math/rand"
	"os"
	"path/filepath"
	"strings"
)

// RandomString 随机生成指定长度字符串
func RandomString(length int, elements string) string {
	letters := []rune(elements)
	b := make([]rune, length)
	for i := range b {
		b[i] = letters[rand.Intn(len(letters))]
	}
	return string(b)
}

// Unzip 解压zip文件到指定目录
func Unzip(file string, dir string) error {
	reader, err := zip.OpenReader(file)
	if err != nil {
		return err
	}
	defer reader.Close()
	prefix := getZipContentPathPrefix(reader)

	for _, f := range reader.File {
		var name = f.Name
		if len(prefix) > 0 && strings.HasPrefix(name, prefix) {
			name = strings.TrimPrefix(name, prefix)
		}
		filePath := filepath.Join(dir, name)
		if f.FileInfo().IsDir() {
			_ = os.MkdirAll(filePath, os.ModePerm)
		} else {
			if err = os.MkdirAll(filepath.Dir(filePath), os.ModePerm); err != nil {
				return err
			}

			inFile, err := f.Open()
			if err != nil {
				return err
			}
			defer inFile.Close()

			outFile, err := os.OpenFile(filePath, os.O_WRONLY|os.O_CREATE|os.O_TRUNC, f.Mode())
			if err != nil {
				return err
			}
			defer outFile.Close()

			_, err = io.Copy(outFile, inFile)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

// 获取zip内容文件前缀
func getZipContentPathPrefix(zipReader *zip.ReadCloser) string {
	paths := getZipContentPaths(zipReader)
	var prefix string
	for i := 0; i < len(paths); i++ {
		path := paths[i]
		hasPrefix := false
		if strings.HasPrefix(path, "__MACOSX") || strings.HasPrefix(path, ".DS_Store") {
			continue
		}

		for j := i + 1; j < len(paths); j++ {
			path2 := paths[j]
			if strings.HasPrefix(path2, "__MACOSX") || strings.HasPrefix(path2, ".DS_Store") {
				continue
			}
			if !strings.HasPrefix(path2, path) {
				break
			}
			if j == len(paths)-1 {
				hasPrefix = true
			}
		}
		if hasPrefix {
			prefix = path
			break
		}
	}
	return prefix
}

// 获取zip内容的文件路径
func getZipContentPaths(zipReader *zip.ReadCloser) []string {
	var fileNames = make([]string, len(zipReader.File))
	for i, f := range zipReader.File {
		fileNames[i] = f.Name
	}
	return fileNames
}
