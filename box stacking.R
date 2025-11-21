library(combinat)
library(dplyr)

rotateBoxes = function(boxes) {
    n=dim(boxes)[1]
    for (i in 1:n) {
      boxes2=permn(boxes[i,])
      boxes2=cbind(sapply(boxes2, `[[`, 1),
                   sapply(boxes2, `[[`, 2),
                   sapply(boxes2, `[[`, 3),
      rep(i,6))
      boxes2 = unique(boxes2)
      if (i==1) {new=boxes2}
      else {new = rbind(new, boxes2)}
    }
    new
}



findMaxHeight = function(boxes, rotate=T) {
  if (rotate) {boxes = rotateBoxes(boxes)}
  else {boxes = cbind(boxes,(1:nrow(boxes)))}
  boxes = boxes[order(boxes[,2]),]
  n = dim(boxes)[1]
  height = boxes[,1]
  width = boxes[,2]
  depth = boxes[,3]
  type = boxes[,4]
  boxlist = rep(NA, n)
  a = rep(NA, n)
  overall_max = 0
  for (i in 1:n) {
    max1 = 0
    boxlist[i] = paste(i)
    if (i>1) {
      for (k in 1:(i-1)) {
        if (width[k]<width[i] & depth[k]<depth[i] & a[k]>max1) {
          max1 = a[k]
          boxlist[i] = paste(boxlist[k], i)
        }
      }
    }
    a[i] = max1 + height[i]
    if (a[i]>overall_max) {overall_max=a[i]}
  }
  d=data.frame(boxlist, a,height,width,depth,type)
  print(d)
  subset(d, a==overall_max, select=c('boxlist','a'))
}


#Examples
boxes=rbind(c(3,3,2), c(4,2,2), c(2,4,4))
x=rotateBoxes(boxes)
x


findMaxHeight(boxes, rotate=F)
findMaxHeight(boxes, rotate=T)


boxes=rbind(c(3,5,4),c(2,3,2),c(2,6,3),c(4,5,1),c(1,4,2),c(2,2,1))
findMaxHeight(boxes, rotate=F)

findMaxHeight(boxes, rotate=T)


