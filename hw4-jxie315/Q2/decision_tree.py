from util import entropy, information_gain, partition_classes
import numpy as np 
import ast
import operator
from scipy.stats import mode

class DecisionTree(object):
#(object):
    def __init__(self):
        # Initializing the tree as an empty dictionary or list, as preferred
        #self.tree = []
        self.tree = {}
        pass








    def learn(self, X, y):
        # TODO: Train the decision tree (self.tree) using the the sample X and labels y
        # You will have to make use of the functions in utils.py to train the tree
        
        # One possible way of implementing the tree:
        #    Each node in self.tree could be in the form of a dictionary:
        #       https://docs.python.org/2/library/stdtypes.html#mapping-types-dict
        #    For example, a non-leaf node with two children can have a 'left' key and  a 
        #    'right' key. You can add more keys which might help in classification
        #    (eg. split attribute and split value)

        #figure out how many attributes are selected in this case

        shape= np.shape(X)
        attr_num=shape[1]
        rows_num=shape[0]

        

     
        





        if entropy(y)==0:
            self.tree['status']='leaf'
            self.tree['value']= y[0]

            return

        elif len(y)==0:
            self.tree["status"]="failed"

            


        elif (X == X[0]).all():
            self.tree['status']= 'leaf'
            self.tree['value']= mode(y)
            return







        else:

            #gain the max of attributes
            #max_value= np.amax(X)
            #min_value=np.amin(X)
             #max_value=max(max_list)

            info_rank={}


            for col_index in range(attr_num):
                info={}
                max_value= np.amax(X[ :, col_index])
                min_value=np.amin(X[:, col_index])




                for attr_val in np.linspace(min_value, max_value, 6):
                

                  inted=partition_classes(X,y,col_index, attr_val)
                  x_left=inted[0]
                  x_right=inted[1]
                  y_left =inted[2]
                  y_right =inted[3]
                  if len(y_left) !=0  and len(y_right) !=0:

                      y_current=[y_left,y_right]
                      #print(y_current)

                      info_gain = information_gain(y, y_current)
                      info[attr_val]=info_gain
        



                if bool(info):
                   best_key= max(info.items(), key=operator.itemgetter(1))[0]
                   info_rank[col_index]=(best_key ,info[best_key])


            
            if bool(info)==False and bool(info_rank)==False:
                print(X)
                print(y)






            #detemine the optimal atrr to split up the data

            optimal_attr=0
            optimal_value=0
            optimal_ig=0
            

            for key, item in info_rank.items():
                if item[1] > optimal_ig:
                    
                   optimal_value= item[0]
                   optimal_attr= key

                pass

            result = partition_classes(X,y, optimal_attr, optimal_value)
            
            #tree_carrier["split_info"]=(optimal_attr, optimal_value)

            left_tree= DecisionTree()
            right_tree=DecisionTree()


            left_tree.learn(result[0], result[2])
            right_tree.learn(result[1], result[3])

            self.tree['status']="normal"
            self.tree['attr']=optimal_attr
            self.tree['attr_val']= optimal_value
            self.tree['left']=left_tree
            self.tree['right']=right_tree


            
            

       

        
        




    def classify(self, record):
        # TODO: classify the record using self.tree and return the predicted label
        if self.tree['status']=='failed':
            return 0
        elif self.tree['status']=='leaf':
            return self.tree['value']
        elif record[self.tree['attr']] <= self.tree['attr_val']:
            return self.tree['left'].classify(record)

        else:
            return self.tree['right'].classify(record)

        
        
        pass











'''X=[[1,2,3],[4,5,6],[7,8,9]]

X= np.asarray(X).reshape(3,3)




y=[]
test=[1,2,3]

classss= DecisionTree()
classss.learn(X,y)

predicted=classss.classify(test)
print(predicted)
'''