def parseGraph():
    with open ("output.csv", "r") as source:
        with open ("graph.csv", "w+") as graph:
            i = 0
            lineList = []
            first = 2
            for line in source:
                line = line[:-1]
                if first > 0:
                    if "Locale" in line:
                        i = 0
                        first -= 1
                        if first != 0:
                            lineList.append(line + ",,,,")
                    else :
                        lineList.append(line + ",")
                    
                
                elif "Locale" in line:
                    i = 0
                    lineList[0] += line + ",,,,"
                else :
                    lineList[i] += line + ","
                i += 1
            
            for newLine in lineList:
                graph.write(newLine)
                graph.write('\n')
            
if __name__ == "__main__":
    parseGraph()